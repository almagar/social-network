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

function Whiteboard() {
    const [isConnected, setIsConnected] = useState(socket.connected);
    const canvasRef = useRef();
    const contextRef = useRef();
    const [isDrawing, setIsDrawing] = useState(false);
    const [color, setColor] = useState('red');
    const [thickness, setThickness] = useState(10);
    const [tool, setTool] = useState('pen');

    useEffect(() => {
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
        socket.on('connection', () => {
            setIsConnected(true);
        });

        socket.on('disconnect', () => {
            setIsConnected(false);
        });

        // sätter lyssnaren en gång men kör arrow functionen varje gpng event kommer

        socket.on('drawPoint', (drawPoint) => {
            console.log('got draw point from socket');

            const { x, y, color, thickness, tool } = drawPoint;

            //const canvas = canvasRef.current;
            //context.scale(2, 2);
            //context.lineCap = 'round';
            //context.strokeStyle = 'black';
            //context.lineWidth = 5;

            if (tool === 'pen') {
                contextRef.current.strokeStyle = color;
                contextRef.current.lineWidth = thickness;
                contextRef.current.beginPath();
                contextRef.current.moveTo(x, y);
                contextRef.current.ellipse(x, y, 0, 0, 0, 0, 0);
                contextRef.current.stroke();
                contextRef.current.closePath();
            } else {
                contextRef.current.globalCompositeOperation = "destination-out";  
                contextRef.current.strokeStyle = "rgba(255,255,255,1)";
            }


        });

        return () => { // när komponenten stängs
            socket.off('connect');
            socket.off('disconnect');
            socket.off('drawPoint');
        };
    }, []); // [] körs en gång när komponenten laddas in

    const onMouseDown = ({ nativeEvent }) => {
        //const { offsetX, offsetY } = nativeEvent;
        //contextRef.current.beginPath();
        //contextRef.current.moveTo(offsetX, offsetY);
        setIsDrawing(true);
    };

    const onMouseUp = () => {
        //contextRef.current.closePath();
        setIsDrawing(false);
    };

    const draw = ({ nativeEvent }) => {
        if (!isDrawing) {
            return;
        }

        const { offsetX, offsetY } = nativeEvent;
        const drawPoint = {
            x: offsetX,
            y: offsetY,
            color: color,
            thickness: thickness,
            tool: tool
        }

        if (tool === 'pen') {
            contextRef.current.globalCompositeOperation = "source-over";
            contextRef.current.strokeStyle = color;
            contextRef.current.lineWidth = thickness;
        } else {
            contextRef.current.globalCompositeOperation = "destination-out";  
            contextRef.current.strokeStyle = "rgba(255,255,255,1)";
            contextRef.current.lineWidth = thickness;
        }

        contextRef.current.beginPath();
        contextRef.current.moveTo(offsetX, offsetY);
        contextRef.current.ellipse(offsetX, offsetY, 0, 0, 0, 0, 0);
        contextRef.current.stroke();
        contextRef.current.closePath();
        socket.emit("drawPoint", drawPoint)
    };

    return (
        <div>
            <p>Connected: {'' + isConnected}</p>

            <div>
                <canvas
                    onMouseDown={onMouseDown}
                    onMouseUp={onMouseUp}
                    onMouseMove={draw}
                    ref={canvasRef}
                    height={300}
                    width={300}
                    style='width: 300px, height: 300px, background: black'
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