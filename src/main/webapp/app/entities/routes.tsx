import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sanctionlist from './sanctionlist';
import AcceptedList from './accepted-list';
import RejectedList from './rejected-list';

export default ({ match }) => {
  return (
    <div>
      <Switch>
        <ErrorBoundaryRoute path={`${match.url}sanctionlist`} component={Sanctionlist} />
        <ErrorBoundaryRoute path={`${match.url}accepted-list`} component={AcceptedList} />
        <ErrorBoundaryRoute path={`${match.url}rejected-list`} component={RejectedList} />
      </Switch>
    </div>
  );
};
