const assert = require('assert');

const os = require('os');

const { Builder, Capabilities } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');

const conf = async url => {
  const localChromedriverPath = `${os.homedir()}\\chromedriver\\chromedriver.exe`;
  chrome.setDefaultService(new chrome.ServiceBuilder(localChromedriverPath).build());

  const chromeCapabilities = Capabilities.chrome();
  chromeCapabilities.set('chromeOptions', { 'w3c': false });

  const driver = await new Builder()
    .forBrowser('chrome')
    .withCapabilities(chromeCapabilities)
    .build();

  await driver.get(url);

  return driver;
};

const todo = require('./todo');

describe('todo', () => {
  let driver;
  let page;

  // open testing app
  before(async () => {
    const url = 'http://todomvc.com/examples/angularjs/#/';
    driver = await conf(url);
    page = await todo(driver);
  });

  after(async () => {
    await driver.quit();
  });

  // add 1 todo
  it('add new todo', async () => {
    const text = 'new todo';
    const newTodo = await page.createTodo(text);
    assert.strictEqual(await newTodo.getText(), text);
    page.deleteTodo();
  });

  // mark as checked 1 todo
  it('check todo', async () => {
    await page.createTodo('new todo');
    await page.toggleCheckbox();
    assert.ok(await page.isTodoCompleted());
    page.deleteTodo();
  });

  // mark as unchecked 1 todo
  it('uncheck todo', async () => {
    await page.createTodo('new todo');
    await page.toggleCheckbox();
    await page.toggleCheckbox();
    assert.ok(!(await page.isTodoCompleted()));
    page.deleteTodo();
  });

  // delete task 
  it('delete todo', async () => {
    const text = 'new todo';
    await page.createTodo(text);
    await page.deleteTodo();
    const todo = await page.findTodo(text);
    assert.ok(!todo);
  });

  // edit task
  it('edit todo', async () => {
    const text = 'edit this task';
    await page.createTodo(text);
    await page.editTodo();
  });

  // mark as checked all active tasks 
  it('check all todoes', async () => {
    await page.createTodo('todo 0');
    await page.createTodo('todo 1');
    await page.toggleAll();
    const todo = await page.findActiveTodo();
    assert.ok(!todo);
    await page.deleteTodo();
    await page.deleteTodo();
  });

  // mark as unchecked all completed tasks 
  it('uncheck all items', async () => {
    await page.createTodo('hello');
    await page.createTodo('goodbye');
    await page.toggleAll();
    await page.toggleAll();
    const todo = await page.findCompletedTodo();
    assert.ok(!todo);
    await page.deleteTodo();
    await page.deleteTodo();
  });

  // displays active 
  it('display only active items', async () => {
    await page.createTodo('todo 2');
    await page.createTodo('todo 3');
    await page.toggleCheckbox();
    await page.selectActiveFilter();
    const todo = await page.findCompletedTodo();
    assert.ok(!todo);
    await page.selectAllFilter();
    await page.deleteTodo();
    await page.deleteTodo();
  });

  // displays completed
  it('display only completed items', async () => {
    await page.createTodo('new todo 1');
    await page.createTodo('new todo 2');
    await page.toggleCheckbox();
    await page.selectCompletedFilter();
    const todo = await page.findActiveTodo();
    assert.ok(!todo);
    await page.selectAllFilter();
    await page.deleteTodo();
    await page.deleteTodo();
  });

  // clears all completed 
  it('delete all completed items at once', async () => {
    await page.createTodo('new todo 1');
    await page.createTodo('new todo 2');
    await page.toggleAll();
    await page.clearCompleted();
    const todoes = await page.getTodoes();
    assert.strictEqual(todoes.length, 0);
  });
});
