import React from 'react';
import '../css/LogIn.css';
import logo from '../img/logo.png';
import { withTranslation } from 'react-i18next';
import { login } from '../actions/LoginActions';
import { connect } from 'react-redux';
import { compose } from 'redux';
import PropTypes from 'prop-types';
// import { login } from '../util/APIUtil';
import FacebookLogin from 'react-facebook-login';
import { Redirect } from 'react-router-dom';
import axios from 'axios';


class LogIn extends React.Component {
    constructor(props) {
        super(props);

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
            },
        }).then(function (response) {
            console.log(response);
        }).catch(function (error) {
            console.log(error);
        });
        window.location.href = "/dashboard";
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
                        {/* <button 
                            type="button" 
                            className="btn btn-lg btn-fb" 
                            style={{color: 'white'}}
                            onClick={this.handleClick}
                        >
                            <i class="fab fa-facebook-f pr-1"></i> 
                            {this.props.t("login.facebook-btn")}
                        </button> */}
                        <FacebookLogin
                            appId="455695445269575"
                            autoLoad={true}
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

LogIn.propTypes = {
    login: PropTypes.func.isRequired
}

const mapStateToProps = state => ({

})

export default
    withTranslation('common')
    // connect(mapStateToProps, { login })
    (LogIn);