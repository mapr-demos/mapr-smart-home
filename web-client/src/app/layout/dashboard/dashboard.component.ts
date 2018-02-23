import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    animations: [routerTransition()]
})
export class DashboardComponent implements OnInit {

    homeTableSettings = {
      delete: {
          confirmDelete: true,
      },
      columns: {
        id: {
          title: 'ID'
        },
        name: {
          title: 'Name'
        },
        address: {
          title: 'Address'
        }
      }
    };

    public homeTableData: Array<any> = [];

    sensorTableSettings = {
      delete: {
          confirmDelete: true,
      },
      columns: {
        id: {
          title: 'ID'
        },
        name: {
          title: 'Name'
        }
      }
    };

    public sensorTableData: Array<any> = [];

    conversionTableSettings = {

      delete: {
          confirmDelete: true,
      },
      columns: {
        expression: {
          title: 'Expression'
        }
      }
    };

    public conversionTableData: Array<any> = [
    ];

    // Must be received from web app
    public homes: Array<any> = [
      {
        id: 1,
        name: "Smart Home #1",
        address: "Fake street 1"
      },
      {
        id: 2,
        name: "Smart Home #2",
        address: "Fake street 2"
      },
      
      {
        id: 3,
        name: "Smart Home #3",
        address: "Fake street 3"
      }
    ];

    public sensors: Array<any> = [
      // First home sensors
      {
        id: 1,
        home_id: 1,
        name: "Temperature sensor of home #1",
        conversions: ["temperature * 10 AS multiplied_temperature"]
      },
      {
        id: 2,
        home_id: 1,
        name: "Brightness sensor of home #1",
        conversions: ["brightness * 1000 AS scaled_brightness"]
      },
      
      {
        id: 3,
        home_id: 1,
        name: "Some sensor of home #1",
        conversions: ["some * 10 AS multiplied_some", "some / 10 AS devided_some"]
      },

      // Second home sensors 
      {
        id: 1,
        home_id: 2,
        name: "Temperature sensor of home #2",
        conversions: ["temperature * 100 AS multiplied_100_temperature"]
      },
      
      {
        id: 2,
        home_id: 2,
        name: "Some sensor of home #2",
        conversions: []
      }
    ];

    selectedHomeId;
    selectedSensorId;

    constructor() {

        Array.prototype.push.apply(this.homeTableData, this.homes);

    }

    ngOnInit() {}

    onHomeSelected(event): void {

        this.selectedHomeId = event.data.id;
        this.sensorTableData = this.sensors.filter(sensor => sensor.home_id === event.data.id)

        this.conversionTableData = []
    }

    onSensorSelected(event): void {

        this.selectedSensorId = event.data.id;
        var homeId = this.selectedHomeId;

        var myArray = this.sensors.filter(sensor => sensor.id === event.data.id && sensor.home_id === homeId).map(s => {
            return s.conversions.map(conversion => {
                return { 
                    expression: conversion 
                }; 
            })
        })

        this.conversionTableData = [].concat.apply([], myArray);
    }

    onHomeDeleteConfirm(event): void {
        if (window.confirm('Are you sure you want to delete the home?')) {
          event.confirm.resolve();
        } else {
          event.confirm.reject();
        }
    }

    onSensorDeleteConfirm(event): void {
        if (window.confirm('Are you sure you want to delete the sensor?')) {
          event.confirm.resolve();
        } else {
          event.confirm.reject();
        }
    }

    onConversionDeleteConfirm(event): void {
        if (window.confirm('Are you sure you want to delete the conversion?')) {
          event.confirm.resolve();
        } else {
          event.confirm.reject();
        }
    }

}
