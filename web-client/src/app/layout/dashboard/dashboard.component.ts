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
      columns: {
        id: {
          title: 'ID'
        },
        homeId: {
          title: 'Home'
        },
        name: {
          title: 'Name'
        }
      }
    };

    public sensorTableData: Array<any> = [];

    conversionTableSettings = {
      columns: {
        sensor: {
          title: 'Sensor'
        },
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
        conversions: ["temperature * 10 AS multiplied_temperature"]
      },
      
      {
        id: 3,
        home_id: 1,
        name: "Some sensor of home #1",
        conversions: ["some * 10 AS multiplied_some", "some / 10 AS devided_some"]
      },

      // Second home sensors 
      {
        id: 2,
        home_id: 2,
        name: "Temperature sensor of home #2",
        conversions: ["temperature * 100 AS multiplied_100_temperature"]
      },
      
      {
        id: 3,
        home_id: 2,
        name: "Some sensor of home #2",
        conversions: []
      }
    ];

    constructor() {

        Array.prototype.push.apply(this.homeTableData, this.homes);

    }

    ngOnInit() {}

    onHomeSelected(event): void {
        this.sensorTableData = this.sensors.filter(sensor => sensor.home_id === event.data.id)
    }

    onSensorSelected(event): void {
        console.log(event)
    }

}
