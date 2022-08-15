import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sanctionlist from './sanctionlist';
import Pendinglist from './pendinglist';

export default ({ match }) => {
  return (
    <div>
      <Switch>
        <ErrorBoundaryRoute path={`${match.url}sanctionlist`} component={Sanctionlist} />
        <ErrorBoundaryRoute path={`${match.url}pendinglist`} component={Pendinglist} />
      </Switch>
    </div>
  );
};
