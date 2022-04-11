import React, { useState } from 'react';
import { Button } from 'react-bootstrap';
import Navbar from "./Navbar";
import "./Error.css";
import Footer from './Footer';
import { useHistory } from 'react-router-dom';


function Error(props: any) {

    const [errMessage, setErrMessage] = useState(undefined as unknown as any);

    let history = useHistory();

    if (history.location.state !== undefined && errMessage === undefined) {
        setErrMessage(history.location.state)
    }

    if (errMessage !== undefined)
      

    return (
        <div className="main__container">
            <Navbar></Navbar>
            <div className="error__container">
                <div>
                    <h2>Upps something went wrong</h2>

                    {errMessage !== undefined && <div className="message__container">
                        <h5>{errMessage[0].status}:{errMessage[0].text} at {errMessage[0].path}</h5>
                    </div>}

                </div>
                <Button className="button__home" onClick={() => window.location.replace("/home")}> Go to Homepage </Button>
            </div>
            <div className="footer">
                <Footer></Footer>
            </div>
        </div >

    );
}

export default Error;