import Home from "./routes/home";
import Adminportal from "./routes/editor";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Footer from "./components/footer";
import React, { useState, useEffect } from 'react';
import LoadingSpinner from './components/spinner/LoadingSpinner';
import { Link } from 'react-router-dom';




  


function App() {
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
      // Simulate loading delay
      setTimeout(() => {
        setIsLoading(false);
      }, 2000);
    }, []);
    return (
        <>
        {isLoading ? (
        <LoadingSpinner />
      ) : (
        <div>
           <Router>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/admin" element={<Adminportal />} />
                </Routes>
            </Router>
            {/* <div className=""> */}

            <Footer/>
            {/* <Link to="/admin">hi</Link> */}
            {/* </div> */}
        </div>
      )}
           
        </>
    );
}

export default App;