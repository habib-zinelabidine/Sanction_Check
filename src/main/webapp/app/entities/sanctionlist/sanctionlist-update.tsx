import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISanctionlist } from 'app/shared/model/sanctionlist.model';
import { getEntity, updateEntity, createEntity, reset } from './sanctionlist.reducer';

export const SanctionlistUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const sanctionlistEntity = useAppSelector(state => state.sanctionlist.entity);
  const loading = useAppSelector(state => state.sanctionlist.loading);
  const updating = useAppSelector(state => state.sanctionlist.updating);
  const updateSuccess = useAppSelector(state => state.sanctionlist.updateSuccess);
  const handleClose = () => {
    props.history.push('/sanctionlist');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...sanctionlistEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...sanctionlistEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterReactApp.sanctionlist.home.createOrEditLabel" data-cy="SanctionlistCreateUpdateHeading">
            <Translate contentKey="jhipsterReactApp.sanctionlist.home.createOrEditLabel">Create or edit a Sanctionlist</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="sanctionlist-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.firstName')}
                id="sanctionlist-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.lastName')}
                id="sanctionlist-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.dob')}
                id="sanctionlist-dob"
                name="dob"
                data-cy="dob"
                type="date"
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.address')}
                id="sanctionlist-address"
                name="address"
                data-cy="address"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.passport')}
                id="sanctionlist-passport"
                name="passport"
                data-cy="passport"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.score')}
                id="sanctionlist-score"
                name="score"
                data-cy="score"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sanctionlist" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SanctionlistUpdate;
