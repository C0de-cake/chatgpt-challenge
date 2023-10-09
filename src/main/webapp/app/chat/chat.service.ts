import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {FlowMessageQueryDTO, FlowMessageResponseDTO} from "./flow-message.model";
import {ApplicationConfigService} from "../core/config/application-config.service";
import {IConversation} from "../entities/conversation/conversation.model";


export type EntityResponseType = HttpResponse<FlowMessageResponseDTO>;
export type RestMessageResponse = FlowMessageResponseDTO;

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
      .post<RestMessageResponse>(this.resourceUrl, messageQuery, { observe: 'response' });
  }
}
