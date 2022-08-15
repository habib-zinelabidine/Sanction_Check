import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      <MenuItem icon="asterisk" to="/sanctionlist">
        <Translate contentKey="global.menu.entities.sanctionlist" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pendinglist">
        <Translate contentKey="global.menu.entities.pendinglist" />
      </MenuItem>
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
