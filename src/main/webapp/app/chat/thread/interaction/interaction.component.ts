import {Component, Input, OnInit} from '@angular/core';
import {IMessage} from "../../../entities/message/message.model";
import {AccountService} from "../../../core/auth/account.service";
import {Account} from "../../../core/auth/account.model";

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

  account: Account | null = null;

  constructor(private accountService: AccountService) {

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
