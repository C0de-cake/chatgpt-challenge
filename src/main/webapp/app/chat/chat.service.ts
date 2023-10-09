import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {FlowMessageQueryDTO, FlowMessageResponseDTO} from "./flow-message.model";
import {ApplicationConfigService} from "../core/config/application-config.service";
import dayjs from "dayjs/esm";
import {map} from "rxjs/operators";
import {IConversation} from "../entities/conversation/conversation.model";
import {RestConversation} from "../entities/conversation/service/conversation.service";

export type EntityResponseType = HttpResponse<FlowMessageResponseDTO>;

type RestOf<T extends FlowMessageResponseDTO > = Omit<T, 'conversation'> & {
  conversation?: RestConversation;
};

export type RestFlowMessageResponse = RestOf<FlowMessageResponseDTO>;

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/flow/message');

  constructor(private http: HttpClient,
              protected applicationConfigService: ApplicationConfigService) {
  }

  sendMessage(messageQuery: FlowMessageQueryDTO): Observable<EntityResponseType> {
    return this.http
      .post<RestFlowMessageResponse>(this.resourceUrl, messageQuery, { observe: 'response' })
      .pipe(map(res =>
        this.convertResponseFromServer(res)));
  }

  protected convertDateFromServer(res: RestFlowMessageResponse | null): FlowMessageResponseDTO {
    if(res !== null) {
      const conversation: IConversation ={
        id:0,
        ...res.conversation,
        createdDate: res.conversation?.createdDate ?
          dayjs.unix(Number(res.conversation.createdDate)) : undefined,
        lastModifiedDate: res.conversation?.lastModifiedDate ?
          dayjs.unix(Number(res.conversation.createdDate)) : undefined,
      }
      return {...res, conversation};
    } else {
      return {};
    }
  }

  protected convertResponseFromServer(res: HttpResponse<RestFlowMessageResponse>): HttpResponse<FlowMessageResponseDTO> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }
}
