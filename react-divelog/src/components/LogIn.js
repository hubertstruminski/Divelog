import React from 'react';
import '../css/LogIn.css';
import logo from '../img/logo.png';
import { withTranslation } from 'react-i18next';

class LogIn extends React.Component {
    constructor() {
        super();

        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit() {

    }

    render() {
        return(
            <div className="login-container">
                <div className="login-form-container form-container-shadow form-size">
                    <div>
                        <span className="logo-text">
                            {this.props.t("login.login")}
                        </span>
                        <br />
                        <img src={logo} alt="divelog" className="logo-space logo" />
                        <br />
                        <span className="logo-text">Divelog</span>
                        <br />
                        <br />
                        <button type="button" class="btn btn-lg btn-fb" style={{color: 'white'}}>
                            <i class="fab fa-facebook-f pr-1"></i> 
                            {this.props.t("login.facebook-btn")}
                        </button>
                    </div>
                </div>
            </div>
        );
    }
}

export default withTranslation('common')(LogIn);