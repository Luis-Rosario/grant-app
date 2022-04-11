import React, { useEffect, useState } from 'react';
import "./Footer.css"

const Clock = ({ date }: { date: Date }) => <div className="clock"> {date.toLocaleTimeString()} </div>


function Footer() {

    const [date, setDate] = useState(new Date())


    useEffect(() => {
        let timeoutHandler = setTimeout(tick, 1000)
        return () => {
            clearTimeout(timeoutHandler)
        }
    }, [date]);

    let tick = () => {
        setDate(new Date())
    }


    return (
        <div className="footer_container">
            <h6 className="date">{date.getDate()}/{date.getMonth()}/{date.getFullYear()}</h6>
            <Clock date={date}></Clock>
        </div>
    )
}


export default Footer;