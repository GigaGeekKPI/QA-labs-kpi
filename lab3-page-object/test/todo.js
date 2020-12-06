const { By, until } = require('selenium-webdriver');

const asyncFind = async (arr, callback) => {
    const results = await Promise.all(arr.map(callback));
    const index = results.findIndex(result => result);
    return arr[index];
};

const selectors = {
    app: By.className('todoapp'),
    todo: {
        item: By.css('.todo-list li'),
        checkbox: By.css('.todo-list li .view input[type=checkbox]'),
        destroy: By.css('.todo-list li .view button.destroy'),
        create: By.className('new-todo')
    },
    edit: By.css('.todo-list li .view label[ng-dblclick]'),
    toggleAll: By.css('label[for=toggle-all]'),
    clearCompleted: By.className('clear-completed'),
    filter: {
        all: By.css('ul.filters li:nth-of-type(1) a'),
        active: By.css('ul.filters li:nth-of-type(2) a'),
        completed: By.css('ul.filters li:nth-of-type(3) a')
    }
};

async function todo(driver) {

    if('AngularJS • TodoMVC' !== await driver.getTitle()) {
        throw 'This is not the todo page!';
    } else {
        await driver.wait(() => until.elementLocated(selectors.app, 1000));
        await driver.sleep(1000);
    }

    const getTodo = async () => {
        return await driver.findElement(selectors.todo.item);
    };

    const getTodoes = async () => {
        return await driver.findElements(selectors.todo.item);
    }

    const createTodo = async text => {
        const newTodoInput = await driver.findElement(selectors.todo.create);
        await newTodoInput.sendKeys(text);
        await newTodoInput.submit();
        return await getTodo();
    };

    const deleteTodo = async () => {
        const todoItem = await getTodo();
        const deleteTodoButton = await driver.findElement(selectors.todo.destroy);
        const text = await todoItem.getText();
        await todoItem.click();
        await deleteTodoButton.click();
        return text;
    };

    const editTodo = async () => {
        const newTodoInput = await driver.findElement(selectors.todo.create);
        await newTodoInput.submit();
        const editTodoButton = await driver.findElement(selectors.edit);
        await editTodoButton.click();
    }

    const toggleCheckbox = async () => {
        const checkbox = await driver.findElement(selectors.todo.checkbox);
        await checkbox.click();
    };

    const toggleAll = async () => {
        const toggleAllButton = await driver.findElement(selectors.toggleAll);
        return await toggleAllButton.click();
    };

    const selectActiveFilter = async () => {
        const activeFilter = await driver.findElement(selectors.filter.active);
        return await activeFilter.click();
    };

    const selectCompletedFilter = async () => {
        const completedFilter = await driver.findElement(selectors.filter.completed);
        return await completedFilter.click();
    };

    const selectAllFilter = async () => {
        const allFilter = await driver.findElement(selectors.filter.all);
        return await allFilter.click();
    };

    const clearCompleted = async () => {
        const clearCompleted = await driver.findElement(selectors.clearCompleted);
        return await clearCompleted.click();
    };

    const isTodoCompleted = async () => {
        const todo = await getTodo();
        const classes = await todo.getAttribute('class');
        return classes.includes('completed');
    };

    const findTodo = async text => {
        const todoes = await getTodoes();
        const todo = await asyncFind(todoes, async element => 
          await element.getText() === text
        );
        return todo;
    };

    const findCompletedTodo = async () => {
        const todoes = await getTodoes();
        const todo = await asyncFind(todoes, async element => {
            const classes = await element.getAttribute('class');
            return classes.includes('completed');
        });
        return todo;
    };

    const findActiveTodo = async () => {
        const todoes = await getTodoes();
        const todo = await asyncFind(todoes, async element => {
            const classes = await element.getAttribute('class');
            return !classes.includes('completed');
        });
        return todo;
    };

    return {
        getTodoes,
        createTodo,
        deleteTodo,
        editTodo,
        toggleCheckbox,
        toggleAll,
        selectAllFilter,
        selectActiveFilter,
        selectCompletedFilter,
        clearCompleted,
        isTodoCompleted,
        findCompletedTodo,
        findActiveTodo,
        findTodo
    }
}

module.exports = todo;