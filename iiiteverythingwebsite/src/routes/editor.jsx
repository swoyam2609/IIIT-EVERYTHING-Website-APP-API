import React, { useState } from "react";
import axios from "axios";
import bgImg from "../assets/telescopeBG.jpg";

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
        "https://dbiiit.swoyam.engineer/checkadmin",
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
          "https://dbiiit.swoyam.engineer/addeditor",
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
        "https://dbiiit.swoyam.engineer/addadmin",
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
        "https://dbiiit.swoyam.engineer/checkeditor",
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
          "https://dbiiit.swoyam.engineer/upload",
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

  // const bgColorClass = 'blue';

  return (
    <div>
      <div
        className="bg-cover  fixed bg-center  "
        style={{
          backgroundImage: `url(${bgImg}) `,
          height: "100vh",
          width: "100vw",
          zIndex: "-10",
        //   filter: "blur(2px)",
        filter: "brightness(0.5)",
        }}
      ></div>
      <div
        className="bg-blue-400  absolute"
        style={{
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
          height: "90vh",
          width: "30vw",
          borderRadius: "15px",
          opacity:".3",
        //   filter: "blur(3px)",
          zIndex: "-1",
          '@media (max-width: 768px)': {
            backgroundColor: 'red',
            opacity: "0",
          },
        }}
      ></div>
      <div className="container flex flex-col mx-auto h-screen  items-center  bg-cover bg-center ">
        <h1 className="text-2xl font-bold p-16 mb-4 text-white cursor-default ">
          ADMIN PORTAL
        </h1>

        <section className="mb-8 flex flex-col items-center ">
          <h2 className="text-xl text-white font-bold mb-2 cursor-default ">
            Manage Admin
          </h2>
          <div>
            <button
              className={`bg-blue-300 hover:bg-blue-400 
                    hover:text-blue-900 text-blue-800 font-bold py-2 px-4 rounded mr-2
                     `}
              onClick={handleAddAdmin}
            >
              Add Admin
            </button>
            <button
              className={`bg-blue-300 hover:bg-blue-400 text-blue-800 hover:text-blue-900
                    
                    font-bold py-2 px-4 rounded mr-2`}
              onClick={handleAddEditor}
            >
              Add Editor
            </button>
          </div>
        </section>

        <section className="mt-16 flex flex-col ">
          <h2 className="text-xl font-bold mb-2 text-white text-center bg-blue-400">
            Upload Files
          </h2>
          <input
            type="file"
            onChange={handleFileUpload}
            className="mb-2 text-white"
          />
          <input
            type="text"
            placeholder="Subject"
            value={subject}
            onChange={handleSubjectChange}
            className=" border-blue-200 border-4 p-2 mb-2"
          />
          <select
            value={docType}
            onChange={handleDocTypeChange}
            className="border border-white p-2 hover:p-4 transition-all duration-300 mb-2   "
          >
            <option value="NOTES">NOTES</option>
            <option value="PAPER">PAPER</option>
            <option value="BOOK">BOOK</option>
          </select>
          <button
            onClick={handleUpload}
            className="bg-blue-500 text-white font-semibold py-2 px-4 rounded mt-16"
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
                <p className="text-red-500 font-bold">{responseMessage}</p>
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
                <p className="text-red-500 font-bold">{responseMessage}</p>
              )}
            </div>
          </div>
        )}

        {/* Verify Editor Modal */}
        {showVerifyModal && (
          <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-gray-500 bg-opacity-50">
            <div className="bg-white p-4 rounded">
              <h2 className="text-xl font-bold mb-2">Verify Editor</h2>
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
                <p className="text-red-500 font-bold">{responseMessage}</p>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Adminportal;
