import React from 'react';
import '../../css/Header.css';
import { Link } from 'react-router-dom';
import { withTranslation } from 'react-i18next';
import $ from 'jquery';
import FB from 'fb';

class HeaderIn extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            accessToken: '',
            email: '',
            name: '',
            userID: '',
            pictureUrl: ''
        }
        this.logout = this.logout.bind(this);
    }

    componentDidMount() {
        let userID = localStorage.getItem("KEY");

        fetch(`/getuserdata/${userID}`, {
            method: 'GET',
            headers: {
              'content-type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(jsonData => {
            console.log(jsonData);
            this.setState({
                accessToken: jsonData.accessToken,
                email: jsonData.email,
                name: jsonData.name,
                userID: jsonData.userID,
                pictureUrl: jsonData.pictureUrl
            });
        }); 

        let isActive = false;
        $(".left-menu-icon").click(function() {

            if(!isActive) {
                $(".left-menu").animate({ left: '0' }, 500);
                isActive = true;
                return;
            }

            if(isActive) {
                $(".left-menu").animate({ left: '-15%' }, 500);
                isActive = false;
                return;
            }
        })
    }


    logout() {
        window.location.href = "https://www.facebook.com/logout.php?next=http://localhost:3000/&access_token=" + this.state.accessToken;
    }


    render() {
        const  { i18n } = this.props;

        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                        <Link to="/home" className="navbar-brand">
                            <span className="logoSize">
                                Divelog
                            </span>
                        </Link>
                        <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggleExternalContent" aria-controls="navbarToggleExternalContent" aria-expanded="false" aria-label="Toggle navigation">
                            <span className="navbar-toggler-icon"></span>
                        </button>
                        <div className="collapse navbar-collapse" id="navbarToggleExternalContent">
                            <ul className="navbar-nav mr-auto">
                                <li className="nav-item mt-3">
                                    <i
                                        onClick={() => i18n.changeLanguage("en")} 
                                        className="gb uk flag nav-link"
                                    />
                                </li>
                                <li className="nav-item mt-3">
                                    <i 
                                        onClick={() => i18n.changeLanguage("de")} 
                                        className="de flag nav-link"
                                    />
                                </li>
                                <li className="nav-item mt-3">
                                    <i 
                                        onClick={() => i18n.changeLanguage("pl")} 
                                        className="pl flag nav-link" 
                                    />
                                </li>
                                <li className="nav-item">
                                    <div className="nav-link left-menu-icon">
                                        <i class="fas fa-bars"></i>
                                    </div>
                                </li>
                            </ul>
                            <ul className="collapse navbar-collapse ul-no justify-content-md-center">
                                <li>
                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="Search"
                                    />
                                </li>
                            </ul>
                            <ul className="navbar-nav my-lg justify-content-end">
                                <li className="nav-item nav-link">
                                    <i class="fas fa-user-friends"></i>
                                </li>
                                <li className="nav-item nav-link">
                                    <i class="fab fa-facebook-messenger"></i>
                                </li>
                                <li className="nav-item nav-link">
                                    <i class="fas fa-bell"></i>
                                </li>
                                <li className="nav-item nav-link">
                                    <img 
                                        src={this.state.pictureUrl} 
                                        alt="Profil" 
                                        className="header-profil-picture"
                                    />
                                </li>
                                <li className="nav-item nav-link">
                                    {this.state.name}
                                </li>
                                <li className="nav-item">
                                    <div className="nav-link">
                                        <button
                                            onClick={this.logout}
                                            className="btn-logout bg-dark"
                                        >
                                            {this.props.t("header.logout")}
                                        </button>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </nav>
                </header>
                <div className="left-menu">
                    <ul className="list-group">
                        <li className="list-group-item item-menu">
                            Social media
                        </li>
                        <li className="list-group-item item-menu">
                            Logbook
                        </li>
                        <li className="list-group-item item-menu">
                            Moja mapa
                        </li>
                        <li className="list-group-item item-menu">
                            Forum
                        </li>
                        <li className="list-group-item item-menu">
                            Ustawienia
                        </li>
                    </ul>
                </div>
            </div>
        );
    }
}

export default withTranslation('common')(HeaderIn);