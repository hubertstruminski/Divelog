import React from 'react';
import '../css/LogIn.css';
import logo from '../img/logo.png';
import { withTranslation } from 'react-i18next';
import FacebookLogin from 'react-facebook-login';
import axios from 'axios';
import { Redirect } from 'react-router-dom';
import { fakeAuth } from '../util/fakeAuth';
import AuthService from '../util/AuthService';

class LogIn extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            redirectToReferrer: false
        }
        this.Auth = new AuthService();
        this.responseFacebook = this.responseFacebook.bind(this);
    }

    // componentWillMount() {
    //     if(this.Auth.loggedIn()) {
    //         this.props.history.replace("/dashboard");
    //     }
    // }

    responseFacebook(response) {
        const loginRequest = {
            accessToken: response["accessToken"],
            email: response["email"],
            name: response["name"],
            userID: response["userID"],
            pictureUrl: response["picture"]["data"]["url"]
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
        })
        fakeAuth.setPrincipal(loginRequest);

        console.log(fakeAuth.userID);

        this.Auth.setToken(loginRequest.accessToken);

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
                            scope="public_profile,user_friends"
                            callback={this.responseFacebook}
                            cssClass="btn btn-lg btn-fb fb-btn-white-font"
                            icon="<i className='fab fa-facebook-f pr-1' />"
                            textButton={this.props.t("login.facebook-btn")}
                        />
                    </div>
                </div>
            </div>
        );
    }
}

export default withTranslation('common')(LogIn);