import {Component, Input, OnInit} from '@angular/core';
import {IMessage} from "../../../entities/message/message.model";
import {AccountService} from "../../../core/auth/account.service";
import {Account} from "../../../core/auth/account.model";
import {MarkdownService} from "ngx-markdown";

@Component({
  selector: 'jhi-interaction',
  templateUrl: './interaction.component.html',
  styleUrls: ['./interaction.component.scss']
})
export class InteractionComponent implements OnInit {

  @Input()
  public message: IMessage = {id: 0};

  @Input()
  public first = false;

  @Input()
  public index = 0;

  account: Account | null = null;

  constructor(private accountService: AccountService, private markdownService: MarkdownService) {

  }

  ngOnInit(): void {
    this.fetchAccountAvatar();
  }

  private fetchAccountAvatar(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
  }
}
