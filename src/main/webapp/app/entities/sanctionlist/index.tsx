import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sanctionlist from './sanctionlist';
import SanctionlistDetail from './sanctionlist-detail';
import SanctionlistUpdate from './sanctionlist-update';
import SanctionlistDeleteDialog from './sanctionlist-delete-dialog';
import SanctionlistSearch from './sanctionlist-search';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SanctionlistUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/search`} component={SanctionlistSearch} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SanctionlistUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SanctionlistDetail} />
      <ErrorBoundaryRoute path={match.url} component={Sanctionlist} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SanctionlistDeleteDialog} />
  </>
);

export default Routes;
