import React from 'react';
import '../../css/Header.css';
import { Link } from 'react-router-dom';
import { withTranslation, Trans } from 'react-i18next';

class Header extends React.Component {
    render() {
        const  { t, i18n } = this.props;
        return (
            <header>
                <nav className="navbar navbar-expand-md navbar-dark bg-primary">
                    <Link to="/home" className="navbar-brand">
                        <span className="logoSize">
                            Divelog
                        </span>
                    </Link>
                    <div className="collapse navbar-collapse">
                        <ul className="navbar-nav navbar-collapse justify-content-start">
                            <li>
                                <i 
                                    onClick={() => i18n.changeLanguage("en")} 
                                    className="gb uk flag" 
                                />
                            </li>
                            <li>
                                <i 
                                    onClick={() => i18n.changeLanguage("de")} 
                                    className="de flag"
                                />
                            </li>
                            <li>
                                <i 
                                    onClick={() => i18n.changeLanguage("pl")} 
                                    className="pl flag" 
                                />
                            </li>
                            <li>
                                <Link to="/home" className="nav-link">
                                    {this.props.t("header.home")}
                                </Link>
                            </li>
                            <li>
                                <Link to="/guide" className="nav-link">
                                    {this.props.t("header.guide")}
                                </Link>
                            </li>
                            <li>
                                <Link to="/about" className="nav-link">
                                    {this.props.t("header.aboutMe")}
                                </Link>
                            </li>
                            <li>
                                <Link to="/home" className="nav-link">
                                    {this.props.t("header.contact")}
                                </Link>
                            </li>
                        </ul>
                        <ul className="navbar-nav navbar-collapse justify-content-end">
                            <li>
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