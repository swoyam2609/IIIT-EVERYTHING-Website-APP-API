import React, { useState } from "react";
import axios from "axios";

function Adminportal() {
    const [file, setFile] = useState(null);
    const [subject, setSubject] = useState("");
    const [docType, setDocType] = useState("NOTES");
    const [newAdminId, setNewAdminId] = useState("");
    const [newAdminPassword, setNewAdminPassword] = useState("");
    const [newAdminName, setNewAdminName] = useState("");
    const [masterPassword, setMasterPassword] = useState("");
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [responseMessage, setResponseMessage] = useState("");

    const handleFileUpload = (event) => {
        const uploadedFile = event.target.files[0];
        setFile(uploadedFile);
    };

    const handleSubjectChange = (event) => {
        const subjectValue = event.target.value;
        setSubject(subjectValue);
    };

    const handleDocTypeChange = (event) => {
        const docTypeValue = event.target.value;
        setDocType(docTypeValue);
    };

    const handleNewAdminIdChange = (event) => {
        setNewAdminId(event.target.value);
    };

    const handleNewAdminPasswordChange = (event) => {
        setNewAdminPassword(event.target.value);
    };

    const handleNewAdminNameChange = (event) => {
        setNewAdminName(event.target.value);
    };

    const handleMasterPasswordChange = (event) => {
        setMasterPassword(event.target.value);
    };

    const handleSubmit = async () => {
        try {
            const payload = {
                name: newAdminName,
                studentid: parseInt(newAdminId),
                password: newAdminPassword,
                adder: 121065,
            };

            const response = await axios.post(
                "http://127.0.0.1:3000/addadmin",
                payload,
                {
                    params: {
                        masterpass: masterPassword,
                    },
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );

            console.log(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleAddAdmin = () => {
        setIsModalOpen(true);
    };

    return (
        <div className="container mx-auto p-8">
            <h1 className="text-2xl font-bold mb-4">Admin Portal</h1>

            <section className="mb-8">
                <h2 className="text-xl font-bold mb-2">Manage Admin</h2>
                <button
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mr-2"
                    onClick={handleAddAdmin}
                >
                    Add Admin
                </button>
                <button
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                    // onClick={handleAddEditor}
                >
                    Add Editor
                </button>
            </section>

            <section>
                <h2 className="text-xl font-bold mb-2">Upload Files</h2>
                <input
                    type="file"
                    onChange={handleFileUpload}
                    className="mb-2"
                />
                <input
                    type="text"
                    placeholder="Subject"
                    value={subject}
                    onChange={handleSubjectChange}
                    className="border border-gray-400 p-2 mb-2"
                />
                <select
                    value={docType}
                    onChange={handleDocTypeChange}
                    className="border border-gray-400 p-2 mb-2"
                >
                    <option value="NOTES">NOTES</option>
                    <option value="PAPER">PAPER</option>
                    <option value="BOOK">BOOK</option>
                </select>
            </section>

            {isModalOpen && (
                <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-gray-500 bg-opacity-50">
                    <div className="bg-white p-4 rounded">
                        <h2 className="text-xl font-bold mb-2">Add Admin</h2>
                        <input
                            type="text"
                            placeholder="ID"
                            value={newAdminId}
                            onChange={handleNewAdminIdChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <input
                            type="password"
                            placeholder="Password"
                            value={newAdminPassword}
                            onChange={handleNewAdminPasswordChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <input
                            type="text"
                            placeholder="Name"
                            value={newAdminName}
                            onChange={handleNewAdminNameChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <input
                            type="password"
                            placeholder="Master Password"
                            value={masterPassword}
                            onChange={handleMasterPasswordChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <button
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                            onClick={handleSubmit}
                        >
                            Submit
                        </button>
                        {responseMessage && <p>{responseMessage}</p>}
                    </div>
                </div>
            )}
        </div>
    );
}

export default Adminportal;
