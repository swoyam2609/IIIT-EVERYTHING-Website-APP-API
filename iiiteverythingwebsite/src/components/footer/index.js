import classes from "./index.module.css";

import React from "react";

export default function Footer() {
    return (
        <>
            <div className={classes.main}>
                <p>
                    {" "}
                    &#169;
                    <a href="/admin">Swoyam Siddharth</a>
                    <a href="/">, Sangdil Biswal</a>, Anirudh Parida
                </p>
            </div>
        </>
    );
}
