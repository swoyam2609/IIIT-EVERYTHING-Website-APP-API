import React, { useState } from "react";
import Lottie from "lottie-react-web";
import animation from "../assets/bg-animation.json";

function Home() {
    const [selectedOption, setSelectedOption] = useState("");
    const [selectedList, setSelectedList] = useState([]);

    const handleOptionChange = (event) => {
        setSelectedOption(event.target.value);
        setSelectedList([]);
    };

    const handleListChange = (event) => {
        const options = Array.from(event.target.options)
            .filter((option) => option.selected)
            .map((option) => option.value);
        setSelectedList(options);
    };

    return (
        <div>
            <h1>Home</h1>
            
            <div>
                <label htmlFor="category">Select Category: </label>
                <select
                    id="category"
                    value={selectedOption}
                    onChange={handleOptionChange}
                >
                    <option value="">Select...</option>
                    <option value="NOTES">NOTES</option>
                    <option value="PAPER">PAPER</option>
                    <option value="BOOK">BOOK</option>
                </select>
            </div>
            <div>
                <label htmlFor="list">Select List:</label>
                <select
                    id="list"
                    multiple
                    value={selectedList}
                    onChange={handleListChange}
                >
                    <option value="Item 1">Item 1</option>
                    <option value="Item 2">Item 2</option>
                    <option value="Item 3">Item 3</option>
                    <option value="Item 4">Item 4</option>
                </select>
            </div>
            <div>
                <Lottie
                    options={{
                        loop: true,
                        autoplay: true,
                        animationData: animation,
                        rendererSettings: {
                            preserveAspectRatio: "xMidYMid slice",
                        },
                    }}
                    speed={0.5}
                />
            </div>
        </div>
    );
}

export default Home;
