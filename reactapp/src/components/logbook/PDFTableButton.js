import React from 'react';
import { withTranslation } from 'react-i18next';
import jsPDF from 'jspdf';
import divelogLogo from '../../img/logo.png';
import ConvertTime from '../../util/ConvertTime';
import $ from 'jquery';
import '../../css/PdfLogbook.css';
import AuthService from '../../util/AuthService';
import { BACKEND_API_URL } from '../../actions/types';

class PDFTableButton extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            partnerName: '',
            partnerSurname: '',
            marker: {},
            entryTime: '',
            exitTime: '',
            averageDepth: 0.0,
            maxDepth: 0.0,
            visibility: 0.0,
            waterTemperature: 0.0,
            airTemperature: 0.0,
            cylinderCapacity: '',
            divingSuit: 'NONE',
            waterType: 'NONE',
            waterEntryType: 'NONE',
            ballast: 0.0,
            glovesType: 'NONE',
            divingType: 'NONE',
            comment: ''
        }
        this.Auth = new AuthService();

        this.ConvertTime = new ConvertTime();
        this.onClick = this.onClick.bind(this);
    }

    renderDataForHTML() {
        return (
            <h1>Hubert Struminski</h1>
        );
    }

    onClick(e) {
        e.preventDefault();

        const id = this.props.id;
        let jwtToken = this.Auth.getRightSocialToken();

        fetch(`${BACKEND_API_URL}/pdf/logbook/${id}/${jwtToken}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
        .then(response => { return response.json() })
        .then(jsonData => {
            let time = this.ConvertTime.convertTime(jsonData.entryTime, jsonData.exitTime, false);

            const markerElement = {
                id: jsonData.marker.id,
                name: jsonData.marker.name,
                latitude: jsonData.marker.latitude,
                longitude: jsonData.marker.longitude
            }

            this.setState({
                partnerName: jsonData.partnerName,
                partnerSurname: jsonData.partnerSurname,
                marker: markerElement,
                entryTime: time[0],
                exitTime: time[1],
                averageDepth: jsonData.averageDepth,
                maxDepth: jsonData.maxDepth,
                visibility: jsonData.visibility,
                waterTemperature: jsonData.waterTemperature,
                airTemperature: jsonData.airTemperature,
                cylinderCapacity: jsonData.cylinderCapacity,
                divingSuit: jsonData.divingSuit,
                waterType: jsonData.waterType,
                waterEntryType: jsonData.waterEntryType,
                ballast: jsonData.ballast,
                glovesType: jsonData.glovesType,
                divingType: jsonData.divingType,
                comment: jsonData.comment
            }, () => {
                let doc = new jsPDF({
                    orientation: "p",
                    unit: "mm",
                });
                let img = new Image();
                img.src = divelogLogo;
 
                let createdTimeDocument = generateDocumentAtTime();

                doc.text("Divelog", 18, 15);
                doc.addImage(img, 'PNG', 15, 17, 25, 25);

                doc.setFontSize(8);
                doc.text(`Generated by Divelog - ${createdTimeDocument}`, 135, 15);

                doc.setFontSize(15);
                doc.text(`Your dive - ${this.state.entryTime}`, 70, 40);
              
                doc.setFontSize(10);
                doc.text(`Partner's name: ${this.state.partnerName}`, 15, 50);
                doc.text(`Partner's surname: ${this.state.partnerSurname}`, 15, 55);
                doc.text(`Entry time: ${this.state.entryTime}`, 15, 60);
                doc.text(`Exit time: ${this.state.exitTime}`, 15, 65);
                doc.text(`Average depth: ${this.state.averageDepth}m`, 15, 70);
                doc.text(`Maximum depth: ${this.state.maxDepth}m`, 15, 75);
                doc.text(`Visibility: ${this.state.visibility}m`, 15, 80);
                doc.text(`Temperature of water: ${this.state.waterTemperature}C`, 15, 85);
                doc.text(`Temperature of air: ${this.state.airTemperature}C`, 15, 90);
                doc.text(`Cylinder capacity: ${this.state.cylinderCapacity}`, 15, 95);
                doc.text(`Diving suit: ${this.state.divingSuit}`, 15, 100);
                doc.text(`Water type: ${this.state.waterType}`, 15, 105);
                doc.text(`Type of entry to water: ${this.state.waterEntryType}`, 15, 110);
                doc.text(`Ballast: ${this.state.ballast}kg`, 15, 115);
                doc.text(`Type of gloves: ${this.state.glovesType}`, 15, 120);
                doc.text(`Diving type: ${this.state.divingType}`, 15, 125);
                doc.text(`Comment: ${this.state.comment}`, 15, 130);

                doc.setFontSize(14);
                doc.text("Google map marker", 85, 150);
                
                doc.setFontSize(10);
                doc.text(`Name: ${this.state.marker.name}`, 15, 160);
                doc.text(`Latitude: ${this.state.marker.latitude}`, 15, 165);
                doc.text(`Longitude: ${this.state.marker.longitude}`, 15, 170);

                let googleStaticMap = new Image();
                googleStaticMap.src = `https://maps.googleapis.com/maps/api/staticmap?center=${this.state.marker.latitude},${this.state.marker.longitude}&zoom=11&size=600x600&markers=color:red%7Clabel:S%7C${this.state.marker.latitude},${this.state.marker.longitude}&key=AIzaSyBgb4kpatKEjsOGsxplxFyRfw1K_wGhLTo`
                googleStaticMap.height = 100;
                googleStaticMap.width = 100;
                doc.addImage(googleStaticMap, 'PNG', 15, 175, 180, 105);
                // doc.fromHTML($(".pdf-logbook-container").html(), 20, 20, { 'width': 500, 'elementHandlers': this.specialElementHandlers });
        
                doc.save("pdf");
            });
        }).catch(err => {
            console.log(err);
        });
    }

    specialElementHandlers = {
        '#hubert': function (element, renderer) {
            return true;
        },
        '.controls': function (element, renderer) {
            return true;
        }
    };

    render() {
        return (
            <div>
                <button className="btn btn-primary"
                    onClick={this.onClick}
                >
                    PDF
                </button>

                <div className="pdf-logbook-container" style={{ display: 'none'}}>
                    <div>
                    </div>
                    <p style={{ fontSize: '25px'}}>Hubert Struminski</p>
                    Partner's name: {this.state.partnerName}
                    <br />
                    Partner's surname: {this.state.partnerSurname}
                    <br />
                    Entry time: {this.state.entryTime}
                    <br />
                    Exit time: {this.state.exitTime}
                    <br />
                    Average depth: {this.state.averageDepth}m
                    <br />
                    Maximum depth: {this.state.maxDepth}m
                    <br />
                    Visibility: {this.state.visibility}m
                    <br />
                    Temperature of water: {this.state.waterTemperature}<sup>o</sup>C
                    <br />
                    Temperature of air: {this.state.airTemperature}<sup>o</sup>C
                    <br />
                    Cylinder capacity: {this.state.cylinderCapacity}
                    <br />
                    Diving suit: {this.state.divingSuit}
                    <br />
                    Water type: {this.state.waterType}
                    <br />
                    Entry type of water: {this.state.waterEntryType}
                    <br />
                    Gloves type: {this.state.glovesType}
                    <br />
                    Ballast: {this.state.ballast}
                    <br />
                    Diving type: {this.state.divingType}
                    <br />
                    Comment: {this.state.comment}
                    <br />
                    Name: {this.state.marker.name}
                    <br />
                    Latitude: {this.state.marker.latitude}
                    <br />
                    Longitude: {this.state.marker.longitude}
                </div>
            </div>
        );
    }
}

function generateDocumentAtTime() {
    let createdAt = new Date();

    let month = createdAt.getMonth();
    if(month < 10) {
        month = "0" + month;
    }

    let day = createdAt.getDay();
    if(day < 10) {
        day = "0" + day;
    }

    let hours = createdAt.getHours();
    if(hours < 10) {
        hours = "0" + hours;
    }

    let minutes = createdAt.getMinutes();
    if(minutes< 10) {
        minutes = "0" + minutes;
    }

    let seconds = createdAt.getSeconds();
    if(seconds < 10) {
        seconds = "0" + seconds;
    }
    return createdAt.getFullYear() + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
}

export default withTranslation("common")(PDFTableButton);