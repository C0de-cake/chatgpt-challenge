import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'conversation',
        data: { pageTitle: 'chatgptChallengeApp.conversation.home.title' },
        loadChildren: () => import('./conversation/conversation.routes'),
      },
      {
        path: 'message',
        data: { pageTitle: 'chatgptChallengeApp.message.home.title' },
        loadChildren: () => import('./message/message.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
