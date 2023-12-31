import React, { useState, useEffect } from "react";
import Lottie from "lottie-react-web";
import animation from "../assets/bg-animation.json";

function Home() {
    const [selectedDocOption, setSelectedDocOption] = useState("");
    const [SubOptions, setSubOptions] = useState([]);
    const [selectedSubOption, setSelectedSubOption] = useState("");
    const [files, setFiles] = useState([]);

    useEffect(() => {
        fetch("https://dbiiit.swoyam.engineer/getsubjects")
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

    function formatDate(uploadDate) {
        const date = new Date(uploadDate);
        const options = { day: "numeric", month: "long" };
        return date.toLocaleDateString("en-US", options);
    }

    async function handleButtonClick() {
        const url = `https://dbiiit.swoyam.engineer/find?sub=${selectedSubOption}&docType=${selectedDocOption}`;
        try {
            const response = await fetch(url);
            const data = await response.json();
            setFiles(data);
        } catch (error) {
            console.error("Error fetching files:", error);
        }
    }

    async function handleDownload(fileid) {
        try {
            const fileId = fileid;
            const downloadUrl = `https://dbiiit.swoyam.engineer/download/${fileId}`;
            const response = await fetch(downloadUrl);
            const blob = await response.blob();
            const url = URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = url;
            link.download = fileId;
            link.target = "_blank";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Error downloading file:", error);
        }
    }

    return (
        <div className="container mx-auto p-4">
            <div className="grid grid-cols-1 md:grid-cols-7">
                <div className="mb-4 w-full mx-2 md:col-span-3">
                    <select
                        id="category"
                        value={selectedDocOption}
                        onChange={handleOptionChange}
                        className="px-2 py-1 border rounded w-full"
                    >
                        <option key={""} value="">
                            Select Document Type
                        </option>
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
                <div className="mb-4 mx-2 md:col-span-3">
                    <select
                        id="category"
                        value={selectedSubOption}
                        onChange={handleListChange}
                        className="px-2 py-1 border rounded w-full"
                    >
                        <option key={""} value="">
                            Select Subject
                        </option>
                        {SubOptions.map((option) => (
                            <option key={option} value={option}>
                                {option}
                            </option>
                        ))}
                    </select>
                </div>
                {/* Button to find the object */}
                <div className="mb-4 mx-auto">
                    <button
                        type="button"
                        className="px-2 py-1 bg-blue-500 text-white rounded"
                        onClick={handleButtonClick}
                    >
                        Find
                    </button>
                </div>
            </div>

            <div className="mt-4">
                {files.map((file, index) => (
                    <div key={index} className="border p-4 rounded-lg mb-4">
                        <p className="font-bold">File Name: {file.filename}</p>
                        <p className="text-gray-600">Sub: {file.sub}</p>
                        <p className="text-gray-600">
                            Document Type: {file.documentType}
                        </p>
                        <p className="text-gray-600">
                            Upload Date: {formatDate(file.upload_date)}
                        </p>
                        <button
                            onClick={() => handleDownload(file._id)}
                            className="px-4 py-2 bg-blue-500 text-white rounded-lg mt-2"
                        >
                            Download
                        </button>
                    </div>
                ))}
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
