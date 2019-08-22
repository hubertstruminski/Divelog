import React from 'react';
import '../css/LogIn.css';
import logo from '../img/logo.png';
import { withTranslation } from 'react-i18next';
import FacebookLogin from 'react-facebook-login';
import axios from 'axios';
import { Redirect } from 'react-router-dom';
import { fakeAuth } from '../util/fakeAuth';

class LogIn extends React.Component {
    constructor() {
        super();

        this.state = {
            redirectToReferrer: false
        }
        this.responseFacebook = this.responseFacebook.bind(this);
    }

    responseFacebook(response) {
        const loginRequest = {
            accessToken: response["accessToken"],
            email: response["email"],
            name: response["name"],
            userID: response["userID"]
        }

        axios({
            method: 'POST',
            url: '/signin',
            data: loginRequest,
            headers: {
                "Accept": "application/json",
                "Content-type": "application/json"
            }
        })

        fakeAuth.authenticate(() => {
            this.setState(() => ({redirectToReferrer: true }));
            console.log(this.state.redirectToReferrer);
        })

        localStorage.setItem("KEY", loginRequest.userID);
        return <Redirect to="/dashboard/" />
    }


    render() {
        const { from } = this.props.location.state || { from: { pathname: '/dashboard' }}
        const { redirectToReferrer } = this.state;

        if(redirectToReferrer === true) {
            return <Redirect to={from} />
        }

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
                        <FacebookLogin
                            appId="455695445269575"
                            autoLoad={false}
                            fields="name,email,picture"
                            callback={this.responseFacebook}
                            render={renderProps => (
                                <button
                                    className="btn btn-lg btn-fb" 
                                    style={{color: 'white'}}
                                >
                                    <i class="fab fa-facebook-f pr-1"></i> 
                                    {this.props.t("login.facebook-btn")}
                                </button>
                            )}
                        />
                    </div>
                </div>
            </div>
        );
    }
}

export default withTranslation('common')(LogIn);