import React from 'react';
import '../css/LogIn.css';
import logo from '../img/logo.png';
import { withTranslation } from 'react-i18next';
import FB from 'fb';
import FacebookLogin from 'react-facebook-login';
import axios from 'axios';
import $ from 'jquery';
import Cookies from 'cookies';
import { Redirect } from 'react-router';
// import { login } from '../actions/LoginActions';
import { connect } from 'react-redux';
import { compose } from 'redux';
import PropTypes from 'prop-types';
import { login } from '../util/APIUtil';

class LogIn extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            username: '',
            password: '',
        }
        this.handleClick = this.handleClick.bind(this);
    }

    // responseFacebook(response) {
    //     console.log(response["accessToken"]);
    //     console.log("response 1: ");
    //     console.log(response);

    //     FB.setAccessToken(response["accessToken"]);

    //     FB.api('me', { fields: 'id,name,email,first_name,location,age_range,birthday,gender', access_token: response["accessToken"] }, function (response2) {
    //         console.log("response 2: ");
    //         console.log(response2);
    //     });

    //     FB.api('me/feed', 'GET', {}, function (response) {
    //         console.log(response);
    //     });
    //     window.location.href = "http://localhost:3000/user";
    // }

    handleClick(e) {
        e.preventDefault();

        // const loginRequest = Object.assign({}, this.state);

        // login(loginRequest)
        // .then(response => {
        //     localStorage.setItem("ACCESS_TOKEN", response.accessToken);
        //     window.location.href = "http://localhost:3000/dashboard";
        // }).catch(error => {
        //     console.log(error);
        // });
        // $.ajaxSetup({
        //     beforeSend : function(xhr, settings) {
        //         if (settings.type == 'POST' || settings.type == 'PUT'
        //             || settings.type == 'DELETE') {
        //             if (!(/^http:.*/.test(settings.url) || /^https:.*/
        //                 .test(settings.url))) {
        //                 // Only send the token to relative URLs i.e. locally.
        //                 xhr.setRequestHeader("X-XSRF-TOKEN",
        //                     Cookies.get('XSRF-TOKEN'));
        //             }
        //         }
        //     }
        // });
        // 
        window.location.href = "http://localhost:8080/login/facebook";
        // this.props.login(this.props.history);

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
                        <button 
                            type="button" 
                            className="btn btn-lg btn-fb" 
                            style={{color: 'white'}}
                            onClick={this.handleClick}
                        >
                            <i class="fab fa-facebook-f pr-1"></i> 
                            {this.props.t("login.facebook-btn")}
                        </button>
                        {/* <FacebookLogin
                            appId="828826390847961"
                            autoLoad={true}
                            callback={this.responseFacebook} 
                            cookie={true}
                            fields="id,name,email,picture"
                            scope="public_profile,user_friends"
                        /> */}
                    </div>
                </div>
            </div>
        );
    }
}

LogIn.propTypes = {
    loginObject: PropTypes.object
}

const mapStateToProps = state => ({
    loginObject: state.loginObject
})

export default compose(
    withTranslation('common'), 
    connect(mapStateToProps, { login })
    )(LogIn);