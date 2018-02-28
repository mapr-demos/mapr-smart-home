import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import {HomeService} from '../../services/home.service';
import {SensorService} from '../../services/sensor.service';
import {Home} from '../../models/home';
import {Sensor} from '../../models/sensor';


@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    animations: [routerTransition()]
})
export class DashboardComponent implements OnInit {

    homeTableSettings = {
      add: {
        confirmCreate: true
      },
      edit:  {
        confirmSave: true
      },
      delete: {
          confirmDelete: true,
      },
      columns: {
        id: {
          title: 'ID',
          editable: false
        },
        name: {
          title: 'Name'
        },
        address: {
          title: 'Address'
        }
      }
    };

    sensorTableSettings = {
      add: {
        confirmCreate: true
      },
      edit:  {
        confirmSave: true
      },
      delete: {
          confirmDelete: true,
      },
      columns: {
        id: {
          title: 'ID',
          editable: false
        },
        name: {
          title: 'Name'
        }
      }
    };

    conversionTableSettings = {
      add: {
        confirmCreate: true
      },
      edit:  {
        confirmSave: true
      },
      delete: {
          confirmDelete: true,
      },
      columns: {
        expression: {
          title: 'Expression'
        }
      }
    };

    public homeTableData: Array<Home> = [];
    public sensorTableData: Array<Sensor> = [];
    public conversionTableData: Array<any> = [];
   
    private selectedHomeId: string;
    private selectedSensor: Sensor;

    constructor(private homeService: HomeService, private sensorService: SensorService) {
    }

    ngOnInit() {
     
        this.homeService.getHomes().subscribe((homes) => {
            this.homeTableData = homes;
        });
    }

    onHomeSelected(event): void {

        this.selectedHomeId = event.data.id;
        this.sensorService.getHomeSensors(this.selectedHomeId).subscribe((sensors) => {
            this.sensorTableData = sensors;
        });

        // Clear conversions table
        this.selectedSensor = null;
        this.conversionTableData = [];
    }

    onSensorSelected(event): void {

        this.selectedSensor = event.data;
        var homeId = this.selectedHomeId;

        var myArray = this.sensorTableData.filter(sensor => sensor.id === event.data.id).map(s => {
            return s.conversions.map(conversion => {
                return { 
                    expression: conversion 
                }; 
            })
        })

        this.conversionTableData = [].concat.apply([], myArray);
    }

    // Create handlers 
    onHomeCreateConfirm(event): void {
        if (window.confirm('Are you sure you want to create?')) {
          
          this.homeService.createNewHome(event.newData).then((created) => {
            event.confirm.resolve(created);
          });

        } else {
          event.confirm.reject();
        }
    }

    onSensorCreateConfirm(event): void {

      if(!this.selectedHomeId) {
        window.alert('Home must be selected!')
        event.confirm.reject();
        return;
      }

      if (window.confirm('Are you sure you want to create?')) {

        var sensor : Sensor = {
          id: event.newData.id || "",
          homeId: this.selectedHomeId || "",
          name: event.newData.name || "",
          conversions: []
        };

        this.sensorService.createNewSensor(sensor).then((created) => {
          event.confirm.resolve(created);
        });

      } else {
        event.confirm.reject();
      }
    }

    onConversionCreateConfirm(event): void {

      if(!this.selectedSensor) {
        window.alert('Sensor must be selected!')
        event.confirm.reject();
        return;
      }

      if (window.confirm('Are you sure you want to create?')) {

        this.selectedSensor.conversions.push(event.newData.expression);
        this.sensorService.updateSensor(this.selectedSensor).then((updated) => {
            event.confirm.resolve(event.newData);
        });

      } else {
        event.confirm.reject();
      }
    }

    // Update handlers
    onHomeUpdateConfirm(event): void {
        if (window.confirm('Are you sure you want to save?')) {
          
          this.homeService.updateHome(event.newData).then((updated) => {
            event.confirm.resolve(updated);
          });

        } else {
          event.confirm.reject();
        }
    }

    onSensorUpdateConfirm(event): void {
        if (window.confirm('Are you sure you want to save?')) {
          
          this.sensorService.updateSensor(event.newData).then((updated) => {
            event.confirm.resolve(updated);
          });

          event.confirm.resolve(event.newData);
        } else {
          event.confirm.reject();
        }
    }

    onConversionUpdateConfirm(event): void {
        if (window.confirm('Are you sure you want to save?')) {
          
          var index = this.selectedSensor.conversions.indexOf(event.data.expression);
          this.selectedSensor.conversions.splice(index, 1);
          this.selectedSensor.conversions.push(event.newData.expression)
  
          this.sensorService.updateSensor(this.selectedSensor).then((updated) => {
              event.confirm.resolve(event.newData);
          });

        } else {
          event.confirm.reject();
        }
    }

    // Delete handlers
    onHomeDeleteConfirm(event): void {
        if (window.confirm('Are you sure you want to delete the home?')) {
          this.homeService.deleteHome(event.data.id);
          event.confirm.resolve();
        } else {
          event.confirm.reject();
        }
    }

    onSensorDeleteConfirm(event): void {
        if (window.confirm('Are you sure you want to delete the sensor?')) {
          this.sensorService.deleteSensor(event.data.id);
          event.confirm.resolve();
        } else {
          event.confirm.reject();
        }
    }

    onConversionDeleteConfirm(event): void {
        if (window.confirm('Are you sure you want to delete the conversion?')) {

          var index = this.selectedSensor.conversions.indexOf(event.data.expression);
          this.selectedSensor.conversions.splice(index, 1);
  
          this.sensorService.updateSensor(this.selectedSensor).then((updated) => {
              event.confirm.resolve();
          });

        } else {
          event.confirm.reject();
        }
    }

}
