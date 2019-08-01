import React from 'react';
import '../../css/Header.css';
import { Link } from 'react-router-dom';
import { withTranslation, Trans } from 'react-i18next';

class Header extends React.Component {
    render() {
        const  { t, i18n } = this.props;
        return (
            <header>
                <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
                    <Link to="/home" className="navbar-brand">
                        <span className="logoSize">
                            Divelog
                        </span>
                    </Link>
                    <button className="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarToggleExternalContent" aria-controls="navbarToggleExternalContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="navbar-collapse collapse" id="navbarToggleExternalContent">
                       <ul className="navbar-nav mr-auto justify-content-start">
                            <li className="nav-item active mt-2">
                                <i
                                    onClick={() => i18n.changeLanguage("en")} 
                                    className="gb uk flag"
                                />
                            </li>
                            <li className="nav-item active mt-2">
                                <i 
                                    onClick={() => i18n.changeLanguage("de")} 
                                    className="de flag"
                                />
                            </li>
                            <li className="nav-item active mt-2">
                                <i 
                                    onClick={() => i18n.changeLanguage("pl")} 
                                    className="pl flag" 
                                />
                            </li>
                            <li className="nav-item active">
                                <Link to="/home" className="nav-link">
                                    {this.props.t("header.home")}
                                </Link>
                            </li>
                            <li className="nav-item active">
                                <Link to="/guide" className="nav-link">
                                    {this.props.t("header.guide")}
                                </Link>
                            </li>
                            <li className="nav-item active">
                                <Link to="/about" className="nav-link">
                                    {this.props.t("header.aboutMe")}
                                </Link>
                            </li>
                            <li className="nav-item active">
                                <Link to="/home" className="nav-link">
                                    {this.props.t("header.contact")}
                                </Link>
                            </li>
                       </ul>
                       <ul className="navbar-nav my-lg justify-content-end">
                            <li className="nav-item active">
                                <Link to="/world_statistics" className="nav-link">
                                    Log In
                                </Link>
                            </li>
                        </ul>
                    </div>
                </nav>
            </header>
        );
    }
}

export default withTranslation('common')(Header);