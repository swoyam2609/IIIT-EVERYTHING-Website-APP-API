import Home from "./routes/home";
import Adminportal from "./routes/editor";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

function App() {
    return (
        <>
            <Router>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/admin" element={<Adminportal />} />
                </Routes>
            </Router>
        </>
    );
}

export default App;