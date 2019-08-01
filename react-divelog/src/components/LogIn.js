import React from 'react';
import '../css/LogIn.css';
import fbLogo from '../img/fb-logo.png';
import logo from '../img/logo.png';

class LogIn extends React.Component {
    render() {
        return(
            <div className="container of-auto">
                <div className="form-container absolute-center form-size">
                </div>
                <div className="form-size absolute-center logo-space">
                    <span className="logo-text">Log In</span>
                    <br />
                    <img src={logo} alt="divelog" className="logo-space logo" />
                    <br />
                    <span className="logo-text">Divelog</span>
                    <br />
                    <br />
                    <button class="ui facebook button">
                        <i class="facebook icon" />
                        Log in with Facebook
                    </button>        
                </div>
            </div>
        );
    }
}

export default LogIn;