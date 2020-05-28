import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Todo from './todo';
import TodoDetail from './todo-detail';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TodoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Todo} />
    </Switch>
  </>
);

export default Routes;
