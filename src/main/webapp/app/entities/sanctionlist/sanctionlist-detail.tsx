import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sanctionlist.reducer';

export const SanctionlistDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const sanctionlistEntity = useAppSelector(state => state.sanctionlist.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sanctionlistDetailsHeading">
          <Translate contentKey="jhipsterReactApp.sanctionlist.detail.title">Sanctionlist</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sanctionlistEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="jhipsterReactApp.sanctionlist.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{sanctionlistEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="jhipsterReactApp.sanctionlist.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{sanctionlistEntity.lastName}</dd>
          <dt>
            <span id="dob">
              <Translate contentKey="jhipsterReactApp.sanctionlist.dob">Dob</Translate>
            </span>
          </dt>
          <dd>
            {sanctionlistEntity.dob ? <TextFormat value={sanctionlistEntity.dob} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="address">
              <Translate contentKey="jhipsterReactApp.sanctionlist.address">Address</Translate>
            </span>
          </dt>
          <dd>{sanctionlistEntity.address}</dd>
          <dt>
            <span id="passport">
              <Translate contentKey="jhipsterReactApp.sanctionlist.passport">Passport</Translate>
            </span>
          </dt>
          <dd>{sanctionlistEntity.passport}</dd>
          <dt>
            <span id="score">
              <Translate contentKey="jhipsterReactApp.sanctionlist.score">Score</Translate>
            </span>
          </dt>
          <dd>{sanctionlistEntity.score}</dd>
        </dl>
        <Button tag={Link} to="/sanctionlist" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sanctionlist/${sanctionlistEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SanctionlistDetail;
