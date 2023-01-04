import { Button, IconButton, Icon, Stack } from '@chakra-ui/react'
import {
    Slider,
    SliderTrack,
    SliderFilledTrack,
    SliderThumb,
} from '@chakra-ui/react'
import { BsFillEraserFill } from "react-icons/bs";
import { useEffect, useRef, useState } from "react"
import axios from "../axiosInstance";
import io from "socket.io-client";

const socket = io("ws://localhost:8000", {
    reconnectionDelayMax: 10000//,
    //auth: {
    //  token: "123"
    //},
    //query: {
    //  "my-key": "my-value"
    //}
});

function Whiteboard({ chatId }) {
    const [isConnected, setIsConnected] = useState(socket.connected);
    const [whiteboardId, setWhiteboardId] = useState(null);

    const canvasRef = useRef();
    const contextRef = useRef();

    const [isDrawing, setIsDrawing] = useState(false);
    const [color, setColor] = useState('red');
    const [thickness, setThickness] = useState(10);
    const [tool, setTool] = useState('pen');

    const drawPointOnCanvas = (drawPoint) => {
        if (drawPoint.tool === 'pen') {
            contextRef.current.globalCompositeOperation = "source-over";
            contextRef.current.strokeStyle = drawPoint.color;
            contextRef.current.lineWidth = drawPoint.thickness;
        } else {
            contextRef.current.globalCompositeOperation = "destination-out";  
            contextRef.current.strokeStyle = "rgba(255,255,255,1)";
            contextRef.current.lineWidth = drawPoint.thickness;
        }
    
        contextRef.current.beginPath();
        contextRef.current.moveTo(drawPoint.x, drawPoint.y);
        contextRef.current.ellipse(drawPoint.x, drawPoint.y, 0, 0, 0, 0, 0);
        contextRef.current.stroke();
        contextRef.current.closePath();
    }

    useEffect(() => {
        axios.get(`http://localhost:8000/whiteboard/chatId/${chatId}`)
        .then(res => {
            setWhiteboardId(res.data.data.id);
        })
        .catch(err => {
            console.log(err)
        })

        const canvas = canvasRef.current;
        canvas.width = window.innerWidth * 2;
        canvas.height = window.innerHeight * 2;
        canvas.style.width = `${window.innerWidth}px`;
        canvas.style.height = `${window.innerHeight}px`;

        const context = canvas.getContext('2d');
        context.scale(2, 2);
        context.lineCap = 'round';
        context.strokeStyle = 'black';
        context.lineWidth = 5;
        contextRef.current = context;
    }, []);

    useEffect(() => {
        if (whiteboardId === null) {
            return;
        }

        axios.get(`http://localhost:8000/whiteboard/${whiteboardId}`)
        .then(res => {
            console.log(res.data.data);
            let drawPoints = res.data.data;
            for (let i = drawPoints.length - 1; i > 0; i--) {
                drawPointOnCanvas(drawPoints[i]);
            }
        })
        .catch(err => {
            console.log(err)
        })

        socket.on('connection', () => {
            setIsConnected(true);
        });

        socket.emit('join', whiteboardId);

        socket.on('disconnect', () => {
            setIsConnected(false);
        });

        socket.on('drawPoint', (drawPoint) => {
            drawPointOnCanvas(drawPoint);
        });

        return () => {
            socket.off('connect');
            socket.off('disconnect');
            socket.off('drawPoint');
        };
    }, [whiteboardId]);

    const onMouseDown = ({ nativeEvent }) => {
        setIsDrawing(true);
    };

    const onMouseUp = () => {
        setIsDrawing(false);
    };

    const draw = ({ nativeEvent }) => {
        if (!isDrawing) {
            return;
        }

        const { offsetX: x, offsetY: y } = nativeEvent;
        const drawPoint = {
            whiteboardId,
            x,
            y,
            color,
            thickness,
            tool
        }

        drawPointOnCanvas(drawPoint);
        socket.emit("drawPoint", drawPoint);
    };

    return (
        <div>
            <div>
                <canvas
                    onMouseDown={onMouseDown}
                    onMouseUp={onMouseUp}
                    onMouseMove={draw}
                    ref={canvasRef}
                    height={300}
                    width={300}
                    style={{ width: "300px !important", height: "300px !important" }}
                />
            </div>

            <Stack direction='row' spacing={4}>
                <Button color='black' borderRadius='20px' onClick={() => { setColor('black'); setTool('pen') }} ></Button>
                <Button colorScheme='red' borderRadius='20px' onClick={() => { setColor('red'); setTool('pen') }}></Button>
                <Button colorScheme='blue' borderRadius='20px' onClick={() => { setColor('blue'); setTool('pen') }}></Button>
                <Button colorScheme='green' borderRadius='20px' onClick={() => { setColor('green'); setTool('pen') }}></Button>
                <IconButton aria-label='Erasor' borderRadius='20px' background="transparent" Icon as={BsFillEraserFill}
                    onClick={() => setTool('erasor')} />
                <Slider aria-label='Size' colorScheme={color} defaultValue={thickness} min={5} max={40}
                    onChangeEnd={(value) => setThickness(value)} width='200px'>
                    <SliderTrack>
                        <SliderFilledTrack />
                    </SliderTrack>
                    <SliderThumb />
                </Slider>
            </Stack>
        </div>
    );
}

export default Whiteboard;