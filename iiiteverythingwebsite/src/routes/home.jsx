import React, { useState, useEffect } from "react";
import Lottie from "lottie-react-web";
import animation from "../assets/bg-animation.json";

function Home() {
    const [selectedDocOption, setSelectedDocOption] = useState("");
    const [SubOptions, setSubOptions] = useState([]);
    const [selectedSubOption, setSelectedSubOption] = useState("");

    useEffect(() => {
        fetch("http://127.0.0.1:3000/getsubjects")
            .then((response) => response.json())
            .then((data) => {
                setSubOptions(data.unique_sub_values);
            })
            .catch((error) => {
                console.error("Error fetching list options:", error);
            });
    }, []);

    const handleOptionChange = (event) => {
        setSelectedDocOption(event.target.value);
    };

    const handleListChange = (event) => {
        setSelectedSubOption(event.target.value);
    };

    return (
        <div className="container mx-auto p-4">
            <div className="grid grid-cols-1">
                <div className="mb-4 w-full">
                    <label htmlFor="category" className="mr-2 mb-1">
                        Select Document Type:
                    </label>
                    <select
                        id="category"
                        value={selectedDocOption}
                        onChange={handleOptionChange}
                        className="px-2 py-1 border rounded w-full"
                    >
                        <option value="">Select...</option>
                        <option key="NOTES" value="NOTES">
                            NOTES
                        </option>
                        <option key="PAPER" value="PAPER">
                            PAPER
                        </option>
                        <option key="BOOK" value="BOOK">
                            BOOK
                        </option>
                    </select>
                </div>
                <div className="mb-4">
                    <label htmlFor="category" className="mr-2">
                        Select Subject:
                    </label>
                    <select
                        id="category"
                        value={selectedDocOption}
                        onChange={handleListChange}
                        className="px-2 py-1 border rounded w-full"
                    >
                        {SubOptions.map((option) => (
                            <option key={option} value={option}>
                                {option}
                            </option>
                        ))}
                    </select>
                </div>
                {/* Button to find the object */}
                <div className="mb-4">
                    <button
                        type="button"
                        className="px-4 py-2 bg-blue-500 text-white rounded"
                    >
                        Find
                    </button>
                </div>
            </div>

            <div className="mb-4 w-1/4 mx-auto">
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
