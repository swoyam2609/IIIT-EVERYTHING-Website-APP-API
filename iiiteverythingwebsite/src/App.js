import Home from "./routes/home";
import Adminportal from "./routes/editor";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Footer from "./components/footer";

function App() {
    return (
        <>
            <Router>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/admin" element={<Adminportal />} />
                </Routes>
            </Router>
            <Footer/>
        </>
    );
}

export default App;