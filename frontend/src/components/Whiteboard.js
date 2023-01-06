import { Button, IconButton, Stack } from '@chakra-ui/react'
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
    reconnectionDelayMax: 10000
});

function Whiteboard({ chatId }) {
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
            contextRef.current.fillStyle = drawPoint.color;
        } else {
            contextRef.current.globalCompositeOperation = "destination-out";  
            contextRef.current.fillStyle = "rgba(255,255,255,1)";
        }
    
        contextRef.current.beginPath();
        contextRef.current.moveTo(drawPoint.x, drawPoint.y);
        contextRef.current.arc(drawPoint.x, drawPoint.y, drawPoint.thickness / 2, 0, 2 * Math.PI);
        contextRef.current.fill();
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

        const width = canvas.parentNode.clientWidth;
        const height = 300;
        canvas.width = width * 2;
        canvas.height = height * 2
        canvas.style.width = `${width}px`;
        canvas.style.height = `${height}px`;

        const context = canvas.getContext('2d');
        context.scale(2, 2);
        context.lineCap = 'round';
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

        socket.emit('join', whiteboardId);

        socket.on('drawPoint', (drawPoint) => {
            drawPointOnCanvas(drawPoint);
        });

        return () => {
            socket.off('drawPoint');
        };
    }, [whiteboardId]);

    const onMouseDown = () => {
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
                />
            </div>

            <Stack direction='row' spacing={4}>
                <Button colorScheme='purple' borderRadius='20px' onClick={() => { setColor('purple'); setTool('pen') }} ></Button>
                <Button colorScheme='red' borderRadius='20px' onClick={() => { setColor('red'); setTool('pen') }}></Button>
                <Button colorScheme='blue' borderRadius='20px' onClick={() => { setColor('blue'); setTool('pen') }}></Button>
                <Button colorScheme='green' borderRadius='20px' onClick={() => { setColor('green'); setTool('pen') }}></Button>
                <IconButton aria-label='Erasor' borderRadius='20px' background="transparent" icon as={BsFillEraserFill}
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