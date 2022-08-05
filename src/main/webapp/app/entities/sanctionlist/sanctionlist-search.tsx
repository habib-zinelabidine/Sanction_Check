import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISanctionlist } from 'app/shared/model/sanctionlist.model';
import sanctionlistReducer, { getEntity, updateEntity, createEntity, reset } from './sanctionlist.reducer';
import Sanctionlist from './sanctionlist';

import axios from 'axios';

export const SanctionlistSearch = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);
  const sanctionlistEntity = useAppSelector(state => state.sanctionlist.entity);
  const loading = useAppSelector(state => state.sanctionlist.loading);
  const updating = useAppSelector(state => state.sanctionlist.updating);
  const updateSuccess = useAppSelector(state => state.sanctionlist.updateSuccess);
  const [search, setsearch] = useState();

  const [firstName, setfirstName] = useState('');
  const [lastName, setlastName] = useState('');
  const [dob, setdob] = useState('');
  const [address, setaddress] = useState('');
  const [passport, setpassport] = useState('');
  const [score, setscore] = useState('');

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

  const handleSearch = event => {
    // const result = sanctionlistEntity.filter(f => f.firstName === sanctionlistEntity.firstName);
    // const entity = {
    //   ...result,

    // get form data

    // dispatch(getEntity(entity));

    const body = {
      apiKey: '5ecfb585-be0e-45cc-b20c-d87e5d5200eb',
      cases: [
        {
          name: firstName,
        },
      ],
    };

    axios.post('https://search.ofac-api.com/v3', body).then(response => console.warn(response.data));
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterReactApp.sanctionlist.home.createOrEditLabel" data-cy="SanctionlistCreateUpdateHeading">
            Search for a Person
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={handleSearch}>
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.firstName')}
                id="sanctionlist-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                onChange={e => {
                  setfirstName(e.target.value);
                }}
                value={firstName}
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.lastName')}
                id="sanctionlist-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                onChange={e => {
                  setlastName(e.target.value);
                }}
                value={lastName}
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.dob')}
                id="sanctionlist-dob"
                name="dob"
                data-cy="dob"
                type="date"
                onChange={e => {
                  setdob(e.target.value);
                }}
                value={dob}
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.address')}
                id="sanctionlist-address"
                name="address"
                data-cy="address"
                type="text"
                onChange={e => {
                  setaddress(e.target.value);
                }}
                value={address}
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.passport')}
                id="sanctionlist-passport"
                name="passport"
                data-cy="passport"
                type="text"
                onChange={e => {
                  setpassport(e.target.value);
                }}
                value={passport}
              />
              <ValidatedField
                label={translate('jhipsterReactApp.sanctionlist.score')}
                id="sanctionlist-score"
                name="score"
                data-cy="score"
                type="text"
                onChange={e => {
                  setscore(e.target.value);
                }}
                value={score}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sanctionlist" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="search-entity" data-cy="entityCreateSaveButton" type="submit">
                <FontAwesomeIcon icon="search" />
                &nbsp; Search
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SanctionlistSearch;
