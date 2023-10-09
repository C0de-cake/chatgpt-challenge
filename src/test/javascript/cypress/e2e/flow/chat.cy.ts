import {IMessage} from "../../../../../main/webapp/app/entities/message/message.model";

describe('Single chat conversation E2e tests', () => {
  const chatPageUrl = '/';
  const username = Cypress.env('E2E_ADMIN_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_ADMIN_PASSWORD') ?? 'user';
  let conversation1;
  let messageFromConversation1: IMessage | undefined;
  const conversationName1 = 'Orchestrator Convertible Soft';

  before(() => {
    cy.login(username, password);
    conversation1 = {
      name: conversationName1
    }

    cy.authenticatedRequest({
      method: 'POST',
      url: `/api/conversations/create`,
      body: conversation1
    }).then(({body}) => conversation1 = body)
      .then(() => {
        messageFromConversation1 = {
          content: 'What\'s Spring boot',
          conversation: conversation1,
          owner: "USER"
        }
        return cy.authenticatedRequest({
          method: 'POST',
          url: `/api/messages`,
          body: messageFromConversation1
        })
      }).then(({body}) => messageFromConversation1 = body);
  });

  after(() => {
    if (messageFromConversation1) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/messages/${messageFromConversation1.id}`,
      }).then(() => {
        messageFromConversation1 = undefined;
      });
    }
    if (conversation1) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/conversations/${conversation1.id}`,
      }).then(() => {
        conversation1 = undefined;
      });
    }
  });

  describe('Single chat', () => {
    beforeEach(() => {
      cy.login(username, password);
      cy.visit(chatPageUrl);
    });

    it('I can send a message directly, it should create a conversation for me and display the answer', () => {
      cy.get('[data-cy="input-to-gpt"]').should('exist');
      cy.get('[data-cy="input-to-gpt"]').type('I want to resume this conversation!');

      cy.get('[data-cy="button-send-to-gpt"]').should('exist');
      cy.get('[data-cy="button-send-to-gpt"]').click();

      cy.get('[data-cy="thread-bubble-0"]').should('exist');
      cy.get('[data-cy="thread-bubble-1"]').should('exist');

      cy.get('[data-cy="thread-bubble-0-user-icon"]').should('exist');
      cy.get('[data-cy="thread-bubble-1-gpt-icon"]').should('exist');
    });

    it('I can send a message from a previous conversation and still see the answer', () => {
      cy.get('[data-cy="conversation-0"]').should('exist');
      cy.get('[data-cy="conversation-0"]').click();

      cy.get('[data-cy="input-to-gpt"]').should('exist');
      cy.get('[data-cy="input-to-gpt"]').type('I want to resume this conversation!');

      cy.get('[data-cy="button-send-to-gpt"]').should('exist');
      cy.get('[data-cy="button-send-to-gpt"]').click();

      cy.get('[data-cy="thread-bubble-1"]').should('exist');
      cy.get('[data-cy="thread-bubble-2"]').should('exist');

      cy.get('[data-cy="thread-bubble-1-user-icon"]').should('exist');
      cy.get('[data-cy="thread-bubble-2-gpt-icon"]').should('exist');
    });
  });
});
