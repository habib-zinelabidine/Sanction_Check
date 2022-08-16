import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/sanctionlist">
        <Translate contentKey="global.menu.entities.sanctionlist" />
      </MenuItem>

      <MenuItem icon="asterisk" to="/accepted-list">
        <Translate contentKey="global.menu.entities.acceptedList" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/rejected-list">
        <Translate contentKey="global.menu.entities.rejectedList" />
      </MenuItem>
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
