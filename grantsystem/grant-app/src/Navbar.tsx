
import React, { useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import "./Navbar.css";
import logo from "./misc/logo.png"
import { Dropdown, DropdownButton } from 'react-bootstrap';


const DropdownGroup = () => {


    const logoutHandler = () => {
        window.localStorage.clear();
        window.location.reload();
        window.location.replace("/login");
    }



    return (
        <>
            {window.localStorage["roles"] !== "ANONYMOUS" &&
                <DropdownButton id="dropdown-basic-button" title="" className="menuDropdown" variant="light">

                    {window.localStorage["roles"].includes("ROLE_REVIEWER") &&
                        <Dropdown.Item onClick={() => window.location.replace("/panels")} >
                            <NavLink className="nav__dropItem" to="/panels"> My Panels</NavLink>
                        </Dropdown.Item>}

                    <Dropdown.Item onClick={() => window.location.replace("/profile")}>
                        <Link className="nav__dropItem" to="/profile">Profile</Link>
                    </Dropdown.Item>

                    <Dropdown.Item onClick={logoutHandler}>
                        <h6>Logout</h6>
                    </Dropdown.Item>


                </DropdownButton>}

            {window.localStorage["roles"] === "ANONYMOUS" && <NavLink className="nav__signIn" to="/login"> Sign in</NavLink>}

        </>
    )
}




function Navbar() {


    return (

        <div className="nav__container">

            <div className="nav__leftSide">
                <label className="nav__title">
                    <NavLink className="nav__home" to="/home">
                        <img className="logo__picture" src={logo} />
                         Grant Management System
                         </NavLink>
                </label>
            </div>

            <div className="nav__rightSide">


                {window.localStorage['roles'] !== 'ANONYMOUS' &&

                    <div className="nav__profileGroup">
                        <span className="nav__profileName">Welcome {window.localStorage['username']}</span>
                        <svg width="1em" height="1em" viewBox="0 0 16 16" className=" nav__profileIcon bi bi-person-circle" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                            <path d="M13.468 12.37C12.758 11.226 11.195 10 8 10s-4.757 1.225-5.468 2.37A6.987 6.987 0 0 0 8 15a6.987 6.987 0 0 0 5.468-2.63z" />
                            <path fillRule="evenodd" d="M8 9a3 3 0 1 0 0-6 3 3 0 0 0 0 6z" />
                            <path fillRule="evenodd" d="M8 1a7 7 0 1 0 0 14A7 7 0 0 0 8 1zM0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8z" />
                        </svg>
                    </div>
                }

                <div className="dropdown__group">
                    <DropdownGroup />
                </div>

            </div>
        </div>

    )

}


export default Navbar;