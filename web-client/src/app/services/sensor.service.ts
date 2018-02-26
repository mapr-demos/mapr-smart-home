import {Injectable} from "@angular/core";
import identity from "lodash/identity";
import {HttpClient} from "@angular/common/http";
import "rxjs/add/operator/toPromise";
import "rxjs/add/operator/map";
import {Observable} from "rxjs";
import find from "lodash/find";
import {Sensor} from "../models/sensor";

const mapToSensor = ({
                      _id,
                      home_id,
                      name,
                      conversions
                    }): Sensor => ({
  id: _id,
  homeId: home_id,
  name,
  conversions
});

const mapToSensorRequest = ({
           id,
           homeId,
           name,
           conversions
         }: Sensor) => ({
  _id: id,
  home_id: homeId,
  name,
  conversions
});

@Injectable()
export class SensorService {

  private static API_URL = 'http://localhost:9000';
  private static SERVICE_URL = '/api/v1/sensors';

  constructor(private http: HttpClient) {
  }

   /**
   * @desc get all sensors from server side
   * */
  getSensors(): Observable<Array<Sensor>>   {
    return this.http
      .get(SensorService.API_URL + SensorService.SERVICE_URL)
      .map((response: any) => {
        return response.map(mapToSensor);
      });
  }

  /**
   * @desc get sensors of the specified home from server side
   * */
  getHomeSensors(homeId: String): Observable<Array<Sensor>>   {
    // FIXME
    return this.http
      .get(SensorService.API_URL + SensorService.SERVICE_URL)
      .map((response: any) => {
        return response.map(mapToSensor).filter(sensor => sensor.homeId === homeId);
      });
  }

  createNewSensor(sensor: Sensor): Promise<Sensor> {
    return this.http
      .post(SensorService.API_URL + SensorService.SERVICE_URL, mapToSensorRequest(sensor))
      .map((response: any) => {
        return mapToSensor(response);
      })
      .toPromise()
  }

  updateSensor(sensor: Sensor): Promise<Sensor> {
    return this.http
      .put(`${SensorService.API_URL}${SensorService.SERVICE_URL}/${sensor.id}`, mapToSensorRequest(sensor))
      .map((response: any) => {
        return mapToSensor(response);
      })
      .toPromise();
  }

  deleteSensor(sensorId: String): Promise<void> {
    return this.http.delete(`${SensorService.API_URL}${SensorService.SERVICE_URL}/${sensorId}`)
      .map(() => {
      })
      .toPromise()
  }
 

}
