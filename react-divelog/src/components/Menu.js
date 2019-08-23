import React from 'react';
import { withTranslation } from 'react-i18next';

class Menu extends React.Component {
    render() {
        return (
            <div>
                <ul className="list-group">
                    <li className="list-group-item item-menu">
                        {this.props.t("menu.socialMedia")}
                    </li>
                    <li className="list-group-item item-menu">
                        {this.props.t("menu.logbook")}
                    </li>
                    <li className="list-group-item item-menu">
                        {this.props.t("menu.map")}
                    </li>
                    <li className="list-group-item item-menu">
                        {this.props.t("menu.forum")}
                    </li>
                    <li className="list-group-item item-menu">
                        {this.props.t("menu.settings")}
                    </li>
                </ul>
            </div>
        );
    }
}

export default withTranslation('common')(Menu);