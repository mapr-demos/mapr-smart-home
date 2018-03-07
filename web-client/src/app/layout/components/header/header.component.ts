import { Component, OnInit, HostListener } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import {Notification} from '../../../models/notification';


@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
    
    public notifications: Array<Notification> = [];
    
    notificationsOpened = false;
    public bellColor = '#989898';

    pushRightClass: string = 'push-right';

    constructor(public router: Router) {
       
        this.router.events.subscribe(val => {
            if (
                val instanceof NavigationEnd &&
                window.innerWidth <= 992 &&
                this.isToggled()
            ) {
                this.toggleSidebar();
            }
        });
    }

    ngOnInit() {

        // TODO WEBSOCKET CONNECTION
        var websocket = new WebSocket("ws://node14:9000/ws");
        websocket.onopen = function(evt) { 
            console.log("OPEN EVENT");
            console.log(evt);
        };

        websocket.onclose =  function(evt) { 
            console.log("ON CLOSE EVENT");
            console.log(evt);
           
        };

        var me = this;
        websocket.onmessage = function(evt) {

               if(!me.notificationsOpened) {
                   me.bellColor = '#e81f1f';
               }

               evt.data.split(/\r?\n/).forEach(jsonNotifocation => {

                    var notification = JSON.parse(jsonNotifocation);
                    console.log(notification);

                    if(me.notifications.length > 2) {
                       me.notifications.shift();
                    } 

                    me.notifications.push(
                    {
                        homeName: notification.homeName,
                        sensorName: notification.sensorName,
                        metrics: JSON.stringify(notification.event.metrics),
                        condition: notification.condition,
                        dateString: new Date().toLocaleString()
                    })
               })

        };

        websocket.onerror = function(evt) { 
            console.log("ERROR EVENT");
            console.log(evt);
        };

    }

    homeNameById(homeId: string) : string {
        return "Some home";
    }


    sensorNameById(sensorId: string) : string {
        return "Some sensor";
    }

    setDefaultBellColor() {
      this.notificationsOpened = !this.notificationsOpened;
      this.bellColor = '#989898';
    }

    isToggled(): boolean {
        const dom: Element = document.querySelector('body');
        return dom.classList.contains(this.pushRightClass);
    }

    toggleSidebar() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle(this.pushRightClass);
    }

    rltAndLtr() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle('rtl');
    }

    onLoggedout() {
        localStorage.removeItem('isLoggedin');
    }

    @HostListener('document:click', ['$event']) 
    onClick (event : MouseEvent) : void {

        if(event.target['id'] == "notifications_list")
          return;

        if(this.notificationsOpened) {
            this.notificationsOpened = false;
        }
    }

}
