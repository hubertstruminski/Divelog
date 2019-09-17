import React from 'react';
import '../../css/Logbook.css';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';
import { Trans } from 'react-i18next';
import { withTranslation } from 'react-i18next';
import DeleteTableButton from './DeleteTableButton';
import UpdateLogbookButton from './UpdateLogbookButton';
import PDFTableButton from './PDFTableButton';

class Logbook extends React.Component {
    constructor() {
        super();
        
        this.state = {
            logbooks: [],
            isEmptyLogbook: true,
            isDeletedRow: false,
            deletedLogbookId: 0
        }
        this.logbooks = [];
        this.bodyTableRef = React.createRef();
        
        this.showTableRows = this.showTableRows.bind(this);
        this.setIsDeletedRow = this.setIsDeletedRow.bind(this);
        this.setDeletedLogbookId = this.setDeletedLogbookId.bind(this);
    }

    componentDidMount() {
        let jwtToken = localStorage.getItem("JwtToken");

        fetch(`/get/logbook/${jwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'content-type': 'application/json'
            }
        }).then(response => response.json())
        .then(jsonData => {
            jsonData.map((jsonElement, index) => {
                const element = {
                    id: jsonElement.id,
                    partnerName: jsonElement.partnerName,
                    partnerSurname: jsonElement.partnerSurname,
                    entryTime: jsonElement.entryTime,
                    exitTime: jsonElement.exitTime,
                    marker: jsonElement.marker.name,
                    averageDepth: jsonElement.averageDepth,
                    maxDepth: jsonElement.maxDepth,
                    airTemperature: jsonElement.airTemperature,
                    waterTemperature: jsonElement.waterTemperature,
                    divingType: jsonElement.divingType
                }
                this.logbooks.push(element);
            });
            if(this.logbooks.length === 0) {
                this.setState({ isEmptyLogbook: true });
            } else {
                if(this.state.isEmptyLogbook === true) {
                    this.setState({ isEmptyLogbook: false });
                }
            }
        });
    }

    componentDidUpdate() {
        for(let i=0; i<this.logbooks.length; i++) {
            if(this.logbooks[i].id === this.state.deletedLogbookId) {
                this.logbooks.splice(i, 1);
            }
        }
        this.bodyTableRef.current = "";
        this.showTable();
    }

    componentWillReceiveProps() {
        this.bodyTableRef.current = "";
        this.showTable();
    }

    showTableRows() {
        let rowNumber = 0;
        return this.logbooks.map((logbook, index) => {
            let entryTime = logbook.entryTime;
            entryTime = entryTime.substr(0, 10) + " " + entryTime.substr(11, 5);

            let exitTime = logbook.exitTime;
            exitTime = exitTime.substr(0, 10) + " " + exitTime.substr(11, 5);
            return (
                <tr key={index}>
                    <th scope="row">{++rowNumber}</th>
                    <td>{logbook.partnerName}</td>
                    <td>{logbook.partnerSurname}</td>
                    <td>{entryTime}</td>
                    <td>{exitTime}</td>
                    <td>{logbook.marker}</td>
                    <td>{logbook.averageDepth}m</td>
                    <td>{logbook.maxDepth}m</td>
                    <td>{logbook.airTemperature}<sup>o</sup>C</td>
                    <td>{logbook.waterTemperature}<sup>o</sup>C</td>
                    <td>{logbook.divingType}</td>
                    <td>
                        <PDFTableButton id={logbook.id} />
                    </td>
                    <td>
                        <UpdateLogbookButton id={logbook.id} />
                    </td>
                    <td>
                        <DeleteTableButton 
                            id={logbook.id} 
                            setIsDeletedRow={this.setIsDeletedRow}
                            setDeletedLogbookId={this.setDeletedLogbookId}
                        />
                    </td>
                </tr>
            );
        });
    }

    showTable() {
        return (
            <div className="table-center table-margin">
                <div className="table-responsive">
                    <table className="table table-hover table-striped">
                        <thead>
                            <tr className="table-primary">
                                <th scope="col">#</th>
                                <td>
                                    {this.props.t("logbook.table.partnerName")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.partnerSurname")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.entryTime")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.exitTime")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.location")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.avgDepth")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.maxDepth")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.airTemperature")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.waterTemperature")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.divingType")}
                                </td>
                                <td>PDF</td>
                                <td>
                                    {this.props.t("logbook.table.UPDATE")}
                                </td>
                                <td>
                                    {this.props.t("logbook.table.DELETE")}
                                </td>
                            </tr>
                        </thead>
                        <tbody ref={this.bodyTableRef}>
                            { this.showTableRows() }
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }

    setIsDeletedRow(value) {
        this.setState({ isDeletedRow: value });
    }

    setDeletedLogbookId(value) {
        this.setState({ deletedLogbookId: value });
    }

    render() {
        let isEmptyLogbook = this.state.isEmptyLogbook;
        let isDeletedRow = this.state.isDeletedRow;

        return (
            <div className="logbook-container">
                <Link 
                    to="/add/dive"
                >
                    <div className="btn btn-primary btn-padding">
                        {this.props.t("logbook.addButton")}
                    </div>
                </Link>
                { !isEmptyLogbook && this.showTable() }   
                <NoLogbookData isEmptyLogbook={this.state.isEmptyLogbook} />
            </div>
        );
    }
}

function NoLogbookData(props) {
    if(props.isEmptyLogbook) {
        return (
            <div className="alert alert-danger alert-margin">
                No data in your logbook.
            </div>
        );
    }
    return null;
}

export default withTranslation("common")(withRouter(Logbook));