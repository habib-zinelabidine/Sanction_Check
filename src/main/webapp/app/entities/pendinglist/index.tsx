import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import Pendinglist from './pendinglist';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute path={match.url} component={Pendinglist} />
    </Switch>
  </>
);

export default Routes;
