import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import TodoComponentsPage from './todo.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../util/utils';

const expect = chai.expect;

describe('Todo e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let todoComponentsPage: TodoComponentsPage;
  let beforeRecordsCount = 0;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
    await waitUntilDisplayed(navBarPage.adminMenu);
    await waitUntilDisplayed(navBarPage.accountMenu);
  });

  it('should load Todos', async () => {
    await navBarPage.getEntityPage('todo');
    todoComponentsPage = new TodoComponentsPage();
    expect(await todoComponentsPage.title.getText()).to.match(/Todos/);

    await waitUntilAnyDisplayed([todoComponentsPage.noRecords, todoComponentsPage.table]);

    beforeRecordsCount = (await isVisible(todoComponentsPage.noRecords)) ? 0 : await getRecordsCount(todoComponentsPage.table);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
