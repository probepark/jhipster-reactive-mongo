import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import TodoComponentsPage, { TodoDeleteDialog } from './todo.page-object';
import TodoUpdatePage from './todo-update.page-object';
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
  let todoUpdatePage: TodoUpdatePage;
  let todoDeleteDialog: TodoDeleteDialog;
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

    expect(await todoComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilAnyDisplayed([todoComponentsPage.noRecords, todoComponentsPage.table]);

    beforeRecordsCount = (await isVisible(todoComponentsPage.noRecords)) ? 0 : await getRecordsCount(todoComponentsPage.table);
  });

  it('should load create Todo page', async () => {
    await todoComponentsPage.createButton.click();
    todoUpdatePage = new TodoUpdatePage();
    expect(await todoUpdatePage.getPageTitle().getAttribute('id')).to.match(/jhipsterApp.todo.home.createOrEditLabel/);
    await todoUpdatePage.cancel();
  });

  it('should create and save Todos', async () => {
    await todoComponentsPage.createButton.click();
    await todoUpdatePage.setTitleInput('title');
    expect(await todoUpdatePage.getTitleInput()).to.match(/title/);
    await todoUpdatePage.setDescriptionInput('description');
    expect(await todoUpdatePage.getDescriptionInput()).to.match(/description/);
    await waitUntilDisplayed(todoUpdatePage.saveButton);
    await todoUpdatePage.save();
    await waitUntilHidden(todoUpdatePage.saveButton);
    expect(await isVisible(todoUpdatePage.saveButton)).to.be.false;

    expect(await todoComponentsPage.createButton.isEnabled()).to.be.true;

    await waitUntilDisplayed(todoComponentsPage.table);

    await waitUntilCount(todoComponentsPage.records, beforeRecordsCount + 1);
    expect(await todoComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);
  });

  it('should delete last Todo', async () => {
    const deleteButton = todoComponentsPage.getDeleteButton(todoComponentsPage.records.last());
    await click(deleteButton);

    todoDeleteDialog = new TodoDeleteDialog();
    await waitUntilDisplayed(todoDeleteDialog.deleteModal);
    expect(await todoDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/jhipsterApp.todo.delete.question/);
    await todoDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(todoDeleteDialog.deleteModal);

    expect(await isVisible(todoDeleteDialog.deleteModal)).to.be.false;

    await waitUntilAnyDisplayed([todoComponentsPage.noRecords, todoComponentsPage.table]);

    const afterCount = (await isVisible(todoComponentsPage.noRecords)) ? 0 : await getRecordsCount(todoComponentsPage.table);
    expect(afterCount).to.eq(beforeRecordsCount);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
