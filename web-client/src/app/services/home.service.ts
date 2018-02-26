import {Injectable} from "@angular/core";
import identity from "lodash/identity";
import {HttpClient} from "@angular/common/http";
import "rxjs/add/operator/toPromise";
import "rxjs/add/operator/map";
import {Observable} from "rxjs";
import find from "lodash/find";
import {Home} from "../models/home";

const mapToHome = ({
                      _id,
                      name,
                      address
                    }): Home => ({
  id: _id,
  name,
  address
});

const mapToHomeRequest = ({
           id,
           name,
           address
         }: Home) => ({
  _id: id,
  name,
  address
});

@Injectable()
export class HomeService {

  private static API_URL = 'http://localhost:9000';
  private static SERVICE_URL = '/api/v1/homes';

  constructor(private http: HttpClient) {
  }

  /**
   * @desc get all smart homes from server side
   * */
  getHomes(): Observable<Array<Home>> {
    return this.http
      .get(HomeService.API_URL + HomeService.SERVICE_URL)
      .map((response: any) => {
        return response.map(mapToHome);
      });
  }

  createNewHome(home: Home): Promise<Home> {
    return this.http
      .post(HomeService.API_URL + HomeService.SERVICE_URL, mapToHomeRequest(home))
      .map((response: any) => {
        return mapToHome(response);
      })
      .toPromise()
  }

  updateHome(home: Home): Promise<Home> {
    return this.http
      .put(`${HomeService.API_URL}${HomeService.SERVICE_URL}/${home.id}`, mapToHomeRequest(home))
      .map((response: any) => {
        return mapToHome(response);
      })
      .toPromise();
  }

  deleteHome(homeId: String): Promise<void> {
    return this.http.delete(`${HomeService.API_URL}${HomeService.SERVICE_URL}/${homeId}`)
      .map(() => {
      })
      .toPromise()
  }

}
