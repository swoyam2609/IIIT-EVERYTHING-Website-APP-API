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
    const [isEditorModalOpen, setIsEditorModalOpen] = useState(false); // Added state for editor modal
    const [adminId, setAdminId] = useState(""); // Added state for admin ID
    const [adminPass, setAdminPass] = useState("");
    const [editorId, setEditorId] = useState(""); // Added state for editor ID
    const [editorPass, setEditorPass] = useState("");
    const [editorName, setEditorName] = useState("");
    const [showVerifyModal, setShowVerifyModal] = useState(false);

    const handleAddEditor = () => {
        setIsEditorModalOpen(true);
    };

    const handleEditorModalClose = () => {
        setIsEditorModalOpen(false);
        setResponseMessage("");
    };

    const handleEditorSubmit = async () => {
        try {
            const checkResponse = await axios.get(
                "http://127.0.0.1:3000/checkadmin",
                {
                    params: {
                        id: adminId,
                        password: adminPass,
                    },
                }
            );

            if (checkResponse.data === true) {
                const payload = {
                    name: editorName,
                    studentid: parseInt(editorId),
                    password: editorPass,
                    adder: adminId,
                };

                const response = await axios.post(
                    "http://127.0.0.1:3000/addeditor",
                    payload,
                    {
                        params: {
                            adder: adminId,
                        },
                        headers: {
                            "Content-Type": "application/json",
                        },
                    }
                );

                setResponseMessage("User Added Successfully");
            } else {
                setResponseMessage("Invalid Admin ID or Password");
            }
        } catch (error) {
            console.error(error);
        }
    };

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

    const handleEditorIdChange = (event) => {
        setEditorId(event.target.value);
    };

    const handleEditorPassChange = (event) => {
        setEditorPass(event.target.value);
    };

    const handleEditorNameChange = (event) => {
        setEditorName(event.target.value);
    };

    const handleAdminIdChange = (event) => {
        setAdminId(event.target.value);
    };

    const handleAdminPassChange = (event) => {
        setAdminPass(event.target.value);
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

            setResponseMessage(response.data.message);
        } catch (error) {
            console.error(error);
        }
    };

    const handleAddAdmin = () => {
        setIsModalOpen(true);
    };

    const handleModalClose = () => {
        setIsModalOpen(false);
        setResponseMessage("");
    };

    const handleFileUploadVerify = async () => {
        try {
            const checkResponse = await axios.get(
                "http://127.0.0.1:3000/checkeditor",
                {
                    params: {
                        id: editorId,
                        password: editorPass,
                    },
                }
            );

            if (checkResponse.data === true) {
                const formData = new FormData();
                formData.append("file", file);

                const queryParams = {
                    sub: subject,
                    docType: docType,
                    uploader: editorId,
                };

                const response = await axios.post(
                    "http://127.0.0.1:3000/upload",
                    formData,
                    {
                        params: queryParams,
                        headers: {
                            "Content-Type": "multipart/form-data",
                        },
                    }
                );

                // Handle the response as needed
                setResponseMessage("File Uploaded Successfully");
            } else {
                setResponseMessage("Invalid Editor ID or Password");
            }
        } catch (error) {
            console.error(error);
        }
    };

    const handleUpload = async () => {
        setShowVerifyModal(true);
    };
    const handleUploadClose = async () => {
        setShowVerifyModal(false);
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
                    onClick={handleAddEditor}
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
                <button
                    onClick={handleUpload}
                    className="bg-blue-500 text-white font-semibold py-2 px-4 rounded"
                >
                    Upload
                </button>
            </section>

            {/* Add Admin modal */}
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
                        <button
                            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                            onClick={handleModalClose}
                        >
                            Close
                        </button>
                        {responseMessage && (
                            <p className="text-red-500 font-bold">
                                {responseMessage}
                            </p>
                        )}
                    </div>
                </div>
            )}

            {/* Add Editor Modal */}
            {isEditorModalOpen && (
                <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-gray-500 bg-opacity-50">
                    <div className="bg-white p-4 rounded">
                        <h2 className="text-xl font-bold mb-2">Add Editor</h2>
                        <input
                            type="text"
                            placeholder="Editor ID"
                            value={editorId}
                            onChange={handleEditorIdChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <input
                            type="password"
                            placeholder="Editor Password"
                            value={editorPass}
                            onChange={handleEditorPassChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <input
                            type="text"
                            placeholder="Editor Name"
                            value={editorName}
                            onChange={handleEditorNameChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <input
                            type="text"
                            placeholder="Admin ID"
                            value={adminId}
                            onChange={handleAdminIdChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <input
                            type="password"
                            placeholder="Admin Password"
                            value={adminPass}
                            onChange={handleAdminPassChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        {/* ... */}
                        <button
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                            onClick={handleEditorSubmit}
                        >
                            Submit
                        </button>
                        <button
                            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                            onClick={handleEditorModalClose}
                        >
                            Close
                        </button>
                        {responseMessage && (
                            <p className="text-red-500 font-bold">
                                {responseMessage}
                            </p>
                        )}
                    </div>
                </div>
            )}

            {/* Verify Editor Modal */}
            {showVerifyModal && (
                <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-gray-500 bg-opacity-50">
                    <div className="bg-white p-4 rounded">
                        <h2 className="text-xl font-bold mb-2">
                            Verify Editor
                        </h2>
                        <input
                            type="text"
                            placeholder="Editor ID"
                            value={editorId}
                            onChange={handleEditorIdChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <input
                            type="password"
                            placeholder="Editor Password"
                            value={editorPass}
                            onChange={handleEditorPassChange}
                            className="border border-gray-400 p-2 mb-2"
                        />
                        <button
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                            onClick={handleFileUploadVerify}
                        >
                            Submit
                        </button>
                        <button
                            className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                            onClick={handleUploadClose}
                        >
                            Close
                        </button>
                        {responseMessage && (
                            <p className="text-red-500 font-bold">
                                {responseMessage}
                            </p>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}

export default Adminportal;
