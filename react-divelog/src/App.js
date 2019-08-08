import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';

import { Provider } from 'react-redux';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import store from './store';

import {I18nextProvider} from 'react-i18next';
import i18next from 'i18next';

import common_de from "./translations/de/common.json";
import common_en from "./translations/en/common.json";
import common_pl from './translations/pl/common.json';

import Header from './components/Layout/Header';
import LogIn from './components/LogIn';
import Home from './components/Layout/Home';

i18next.init({
  interpolation: { escapeValue: false },
  lng: 'en', 
  resources: {
    en: {
      common: common_en
    },
    de: {
      common: common_de
    },
    pl: {
      common: common_pl
    }
  }
});

function App() {
  return (
    <Provider store={store} >
      <Router>
        <div className="App">
          <I18nextProvider i18n={i18next}>
            <Header />
            <Route exact path="/home" component={Home} />
            <Route exact path="/login" component={LogIn} />
          </I18nextProvider>
        </div>
      </Router>
    </Provider>
  );
}

export default App;
