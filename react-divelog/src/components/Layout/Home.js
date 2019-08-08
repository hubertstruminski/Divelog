import React from 'react';
import '../../css/Home.css';
import fbLogo from '../../img/fb-logo.png';
import { withTranslation } from 'react-i18next';
import $ from 'jquery';

class Home extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        // $(".fb-container").hide();
        // $(window).scroll(function() {
        //     var scrollValue = $(window).scrollTop();
        //     this.console.log("scroll: " + scrollValue);

        //     if(scrollValue === 120) {
        //         $(".fb-container").show();
        //         $(".fb-container").animate({ left: '50%' }, 1500);
        //     }     
        // });
    }
    render() {
        return (
            <div className="home-container">
                <div className="jumbotron jumbotron-fluid home-background shadow-jumbotron">
                    <div className="home-grid">
                        <div className="home-grid-item item1-margin">
                        <h1 className="display-3 font-color-jumbotron">
                            {this.props.t("home.built")}
                        </h1>
                        <p className="lead font-color-jumbotron">
                            {this.props.t("home.step")}
                        </p>
                        </div>
                        <div className="home-grid-item"></div>
                        <div className="home-grid-item">
                            <div className="btn-container">
                            <h1 className="display-3 font-color-jumbotron">
                                {this.props.t("home.let")}
                            </h1>
                                <button type="button" class="btn btn-lg btn-fb" style={{color: 'white'}}>
                                    <i class="fab fa-facebook-f pr-1"></i> 
                                    {this.props.t("login.facebook-btn")}
                                </button>
                            </div>
                       </div>
                    </div>
                </div>

                <div className="grid-container fb-center">
                    <div className="fb-item1">
                        <h1 className="display-4">
                            {this.props.t("home.fb-title")}
                        </h1>
                        <p className="fb-pgh">
                            {this.props.t("home.fb-paragraph")}
                        </p>
                    </div>
                    <div className="fb-item2"></div>
                </div>

                <div className="grid-container book-center">
                    <div className="fb-item1">
                        <h1 className="display-4">
                            {this.props.t("home.logbook")}
                        </h1>
                        <p className="fb-pgh">
                            {this.props.t("home.logbook-paragraph")}
                        </p>
                    </div>
                    <div className="item4"></div>
                </div>

                <div className="grid-container maps-center">
                    <div className="fb-item1">
                        <h1 className="display-4">
                            {this.props.t("home.maps")}
                        </h1>
                        <p className="fb-pgh">
                            {this.props.t("home.maps-paragraph")}
                        </p>
                    </div>
                    <div className="item6"></div>
                </div>

                <div className="grid-container computer-center">
                    <div className="fb-item1">
                        <h1 className="display-4">
                            {this.props.t("home.computer")}
                            <br />
                            <span style={{color: 'red', fontSize: '1.3vw', fontWeight: '800'}}>
                                {this.props.t("home.progress")}
                            </span>
                        </h1>
                        <p className="fb-pgh">
                            {this.props.t("home.computer-paragraph")}
                        </p>
                    </div>
                    <div className="item8"></div>
                </div>

                <div className="grid-container forum-center">
                    <div className="fb-item1">
                        <h1 className="display-4">
                            {this.props.t("home.forum")}
                        </h1>
                        <p className="fb-pgh">
                            {this.props.t("home.forum-paragraph")}
                        </p>
                    </div>
                    <div className="item10"></div>
                </div>

                <div className="footer footer-shadow">
                    &copy; {this.props.t("home.footer")}
                </div>
            </div>
        );
    }
}

export default withTranslation('common')(Home);