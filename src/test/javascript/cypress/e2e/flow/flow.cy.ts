import {IConversation} from "../../../../../main/webapp/app/entities/conversation/conversation.model";

describe('Flow conversation e2e test', () => {
  const chatPageUrl = '/';
  const username = Cypress.env('E2E_ADMIN_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_ADMIN_PASSWORD') ?? 'user';

  let conversation1;
  let conversation2;
  let conversation3;

  const conversationName1 = 'Orchestrator Convertible Soft';
  const conversationName2 = 'What\'s jhipster?';
  const conversationName3 = 'What\'s Spring boot?';

  before(() => {
    cy.login(username, password);
    conversation1 = {
      name: conversationName1,

    }
    conversation2 = {
      name: conversationName2
    }
    conversation3 = {
      name: conversationName3
    }

    cy.authenticatedRequest({
      method: 'POST',
      url: `/api/conversations/create`,
      body: conversation1
    }).then( ({body}) =>   conversation1 = body);

    cy.authenticatedRequest({
      method: 'POST',
      url: `/api/conversations/create`,
      body: conversation2
    }).then( ({body}) =>   conversation2 = body);

    cy.authenticatedRequest({
      method: 'POST',
      url: `/api/conversations/create`,
      body: conversation3
    }).then( ({body}) =>   conversation3 = body);
  });

  after(() => {
    if (conversation1) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/conversations/${conversation1.id}`,
      }).then(() => {
        conversation1 = undefined;
      });
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/conversations/${conversation2.id}`,
      }).then(() => {
        conversation2 = undefined;
      });
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/conversations/${conversation3.id}`,
      }).then(() => {
        conversation3 = undefined;
      });
    }
  });

  describe('Flow conversation', () => {
    beforeEach(() => {
      cy.login(username, password);
      cy.visit(chatPageUrl);
    });

    it('some conversations should be present', () => {
      cy.get('[data-cy="conversation-0"]').should('exist');
      cy.get('[data-cy="conversation-1"]').should('exist');
      cy.get('[data-cy="conversation-2"]').should('exist');
    });

    it('I can rename of conversation', () => {
      const renameConversationName = 'JHipster definition';

      cy.get('[data-cy="conversation-0"]').should('exist');
      cy.get('[data-cy="conversation-1"]').should('exist');
      cy.get('[data-cy="conversation-2"]').should('exist');

      cy.get('[data-cy="conversation-0"] [data-cy="name"]')
        .should("have.text", conversationName3);

      cy.get('[data-cy="conversation-0"] [data-cy="name"]').click()

      cy.get('[data-cy="conversation-0"] [data-cy="rename"]').should('exist');
      cy.get('[data-cy="conversation-0"] [data-cy="rename"]').click();
      cy.get('[data-cy="conversation-0"] [data-cy="rename-input"]').should('exist');
      cy.get('[data-cy="conversation-0"] [data-cy="rename-input"]').clear()
      cy.get('[data-cy="conversation-0"] [data-cy="rename-input"]')
        .type(renameConversationName);
      cy.get('[data-cy="conversation-0"] [data-cy="validate-rename"]').click();

      cy.get('[data-cy="conversation-0"] [data-cy="name"]')
        .should("have.text", renameConversationName);
    });

    it('I can delete a conversation', () => {
      cy.get('[data-cy="conversation-0"]').should('exist');
      cy.get('[data-cy="conversation-1"]').should('exist');
      cy.get('[data-cy="conversation-2"]').should('exist');

      cy.get('[data-cy="conversation-2"] [data-cy="name"]').click()

      cy.get('[data-cy="conversation-2"] [data-cy="delete"]').should('exist');
      cy.get('[data-cy="conversation-2"] [data-cy="delete"]').click();
      cy.get('[data-cy="conversation-2"]').should('not.exist');
    });
  });
});
